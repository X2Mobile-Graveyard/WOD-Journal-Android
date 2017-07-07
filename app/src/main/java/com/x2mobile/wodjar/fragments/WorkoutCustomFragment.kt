package com.x2mobile.wodjar.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.WorkoutEditActivity
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.WorkoutCustomRequestEvent
import com.x2mobile.wodjar.data.event.WorkoutsCustomRequestEvent
import com.x2mobile.wodjar.data.event.WorkoutsCustomRequestFailureEvent
import com.x2mobile.wodjar.data.event.base.RequestResponseEvent
import com.x2mobile.wodjar.data.model.WorkoutCustom
import com.x2mobile.wodjar.data.model.WorkoutType
import com.x2mobile.wodjar.fragments.base.WorkoutBaseFragment
import com.x2mobile.wodjar.ui.binding.model.WorkoutViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.intentFor
import kotlin.reflect.KClass

class WorkoutCustomFragment : WorkoutBaseFragment<WorkoutCustom>() {

    val REQUEST_CODE_EDIT_WORKOUT = 29

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Preference.isLoggedIn(context)) {
            Service.getWorkoutCustom(workout.id)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbarDelegate.title = getString(R.string.workout_custom)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_edit, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.edit_menu).isVisible = workout.type == WorkoutType.CUSTOM
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_menu -> {
                startActivityForResult(context.intentFor<WorkoutEditActivity>(NavigationConstants.KEY_WORKOUT to workout), REQUEST_CODE_EDIT_WORKOUT)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_EDIT_WORKOUT) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val cachedWorkout = data?.getParcelableExtra<WorkoutCustom>(NavigationConstants.KEY_WORKOUT)!!
                    workout.description = cachedWorkout.description
                    workout.imageUri = cachedWorkout.imageUri
                    workout.date = cachedWorkout.date
                    arguments.putParcelable(NavigationConstants.KEY_WORKOUT, workout)

                    binding.viewModel = WorkoutViewModel(context, workout)

                    imageViewer.imageUri = workout.imageUri
                }
                NavigationConstants.RESULT_DELETED -> activity.finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun getWorkoutResultTitle(): String {
        return getString(R.string.workout_result)
    }

    override fun getRequestEventType(): KClass<out RequestResponseEvent<MutableList<WorkoutCustom>>> {
        return WorkoutsCustomRequestEvent::class
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutResponse(requestResponseEvent: WorkoutCustomRequestEvent) {
        handleWorkoutResponse(requestResponseEvent)
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutFailure(requestFailureEvent: WorkoutsCustomRequestFailureEvent) {
        handleRequestFailure(requestFailureEvent.throwable)
    }

}