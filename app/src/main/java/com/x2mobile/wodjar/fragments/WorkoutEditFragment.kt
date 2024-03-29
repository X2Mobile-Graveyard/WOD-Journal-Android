package com.x2mobile.wodjar.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.business.Constants
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.business.network.AmazonService
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.*
import com.x2mobile.wodjar.data.model.WorkoutCustom
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
import org.jetbrains.anko.support.v4.toast
import java.util.*

class WorkoutEditFragment : BaseFragment(), DatePickerDialog.OnDateSetListener {

    private val DIALOG_DATE_PICKER = "date_picker"

    val workout: WorkoutCustom by lazy {
        savedArguments?.get(NavigationConstants.KEY_WORKOUT) as WorkoutCustom? ?:
                arguments?.get(NavigationConstants.KEY_WORKOUT) as WorkoutCustom? ?: WorkoutCustom()
    }

    lateinit var binding: WorkoutEditBinding

    val viewModel: WorkoutEditViewModel by lazy { WorkoutEditViewModel(workout) }

    private val imagePicker: ImagePicker by lazy { ImagePicker(this, savedArguments) }

    private val imageViewer: ImageViewer by lazy { ImageViewer(this, binding.image) }

    private var progress: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        EventBus.getDefault().register(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbarDelegate.title = if (workout.id < 0) getString(R.string.create_workout) else getString(R.string.modify_workout)
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

        binding.date.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = workout.date
            DatePickerDialog.newInstance(this@WorkoutEditFragment, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show(fragmentManager, DIALOG_DATE_PICKER)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        imagePicker.onSaveInstance(outState)
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
        inflater.inflate(R.menu.menu_workout, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.delete_menu).isVisible = workout.id >= 0
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.done_menu -> {
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

            R.id.delete_menu -> {
                //Updating the cached version
                val workouts = EventBus.getDefault().getStickyEvent(WorkoutsCustomRequestEvent::class.java).response.body()!!
                workouts.removeAll { it.id == workout.id }

                Service.deleteWorkout(workout.id)

                activity.setResult(NavigationConstants.RESULT_DELETED)
                activity.finish()
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

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        workout.date = calendar.time
        viewModel.notifyDateChange()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutAdded(requestResponseEvent: AddWorkoutRequestEvent) {
        progress?.dismiss()
        if (requestResponseEvent.response.body() != null) {

            //Updating the cached version
            val workouts = EventBus.getDefault().getStickyEvent(WorkoutsCustomRequestEvent::class.java).response.body()!!
            workouts.add(requestResponseEvent.response.body()!!)

            activity.finish()
        } else {
            toast(R.string.error_occurred)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutUpdated(requestResponseEvent: UpdateWorkoutRequestEvent) {
        progress?.dismiss()

        //Updating the cached version
        val workouts = EventBus.getDefault().getStickyEvent(WorkoutsCustomRequestEvent::class.java).response.body()!!
        val cachedWorkout = workouts.find { it.id == workout.id }
        cachedWorkout?.description = workout.description
        cachedWorkout?.date = workout.date
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

    private fun saveWorkout(workout: WorkoutCustom) = if (workout.id != Constants.ID_NA) {
        Service.updateWorkout(workout)
    } else {
        Service.saveWorkout(workout)
    }

    private fun isInputValid(): Boolean {
        if (TextUtils.isEmpty(binding.description.text)) {
            binding.description.error = getString(R.string.field_mandatory)
            return false
        }
        return true
    }
}
