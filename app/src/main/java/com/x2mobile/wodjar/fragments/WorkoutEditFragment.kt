package com.x2mobile.wodjar.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.business.Constants
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.business.network.AmazonService
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.*
import com.x2mobile.wodjar.data.model.Workout
import com.x2mobile.wodjar.databinding.WorkoutEditBinding
import com.x2mobile.wodjar.fragments.base.BaseFragment
import com.x2mobile.wodjar.ui.binding.model.WorkoutEditViewModel
import com.x2mobile.wodjar.ui.helper.ImagePicker
import com.x2mobile.wodjar.ui.helper.ImageViewer
import com.x2mobile.wodjar.util.isUrl
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class WorkoutEditFragment : BaseFragment() {

    val workout: Workout by lazy {
        savedArguments?.get(NavigationConstants.KEY_WORKOUT) as Workout? ?: arguments?.get(NavigationConstants.KEY_WORKOUT) as Workout? ?:
                Workout().apply { name = getString(R.string.workout) }
    }

    lateinit var binding: WorkoutEditBinding

    val viewModel: WorkoutEditViewModel by lazy { WorkoutEditViewModel(workout) }

    val imagePicker: ImagePicker by lazy { ImagePicker(this) }

    val imageViewer: ImageViewer by lazy { ImageViewer(this, binding.image) }

    var progress: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        EventBus.getDefault().register(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbarDelegate.enableTitleChange()
        toolbarDelegate.title = workout.name!!
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.workout_edit, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewer.imageUri = workout.imageUri

        binding.addImage.setOnClickListener {
            imagePicker.addPicture(this)
        }

        binding.removeImage.setOnClickListener {
            workout.imageUri = null
            viewModel.notifyImageChange()
        }

        binding.image.setOnClickListener {
            imageViewer.popup(binding.imageContainer)
        }

        binding.delete.setOnClickListener {
            //Updating the cached version
            val workouts = EventBus.getDefault().getStickyEvent(WorkoutsRequestEvent::class.java).response.body()!!.workouts
            workouts.removeIf { it.id == workout.id }

            Service.deleteWorkout(workout.id)

            activity.setResult(NavigationConstants.RESULT_DELETED)
            activity.finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(NavigationConstants.KEY_WORKOUT, workout)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        imagePicker.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imagePicker.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_done, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.done_menu -> {
                workout.name = toolbarDelegate.title
                if (isInputValid()) {
                    progress = context.indeterminateProgressDialog(R.string.saving)
                    doAsync {
                        if (workout.imageUri != null && !workout.imageUri!!.isUrl()) {
                            AmazonService.upload(this, workout.imageUri!!, { uri ->
                                workout.imageUri = uri
                                saveWorkout(workout)
                            })
                        } else {
                            saveWorkout(workout)
                        }
                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onImageSet(event: ImageSetEvent) {
        workout.imageUri = event.uri
        imageViewer.imageUri = event.uri
        viewModel.notifyImageChange()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTitleSet(event: TitleSetEvent) {
        workout.name = toolbarDelegate.title
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutAdded(requestResponseEvent: AddWorkoutRequestEvent) {
        progress?.dismiss()
        if (requestResponseEvent.response.body() != null) {

            //Updating the cached version
            val workouts = EventBus.getDefault().getStickyEvent(WorkoutsRequestEvent::class.java).response.body()!!.workouts
            workouts.add(requestResponseEvent.response.body()!!)

            activity.finish()
        } else {
            context.toast(R.string.error_occurred)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutUpdated(requestResponseEvent: UpdateWorkoutRequestEvent) {
        progress?.dismiss()

        //Updating the cached version
        val workouts = EventBus.getDefault().getStickyEvent(WorkoutsRequestEvent::class.java).response.body()!!.workouts
        val cachedWorkout = workouts.find { it.id == workout.id }
        cachedWorkout?.name = workout.name
        cachedWorkout?.description = workout.description
        cachedWorkout?.imageUri = workout.imageUri

        activity.setResult(Activity.RESULT_OK, context.intentFor<Any>(NavigationConstants.KEY_WORKOUT to workout))
        activity.finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutAddFailed(requestFailureEvent: AddWorkoutRequestFailureEvent) {
        progress?.dismiss()
        handleRequestFailure(requestFailureEvent.throwable)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutUpdateFailed(requestFailureEvent: UpdateWorkoutResultRequestFailureEvent) {
        progress?.dismiss()
        handleRequestFailure(requestFailureEvent.throwable)
    }

    fun saveWorkout(workout: Workout) {
        if (workout.id != Constants.ID_NA) {
            Service.updateWorkout(workout)
        } else {
            Service.saveWorkout(workout)
        }
    }

    private fun isInputValid(): Boolean {
        if (TextUtils.isEmpty(binding.description.text)) {
            binding.description.error = getString(R.string.field_mandatory)
            return false
        }
        return true
    }
}
