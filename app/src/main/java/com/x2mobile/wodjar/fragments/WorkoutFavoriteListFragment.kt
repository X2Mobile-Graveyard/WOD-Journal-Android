package com.x2mobile.wodjar.fragments

import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.WorkoutActivity
import com.x2mobile.wodjar.activity.WorkoutCustomActivity
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.data.event.WorkoutsRequestEvent
import com.x2mobile.wodjar.data.model.Workout
import com.x2mobile.wodjar.data.model.WorkoutType
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class WorkoutFavoriteListFragment : WorkoutListFragment() {

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    override fun onWorkoutsResponse(requestResponseEvent: WorkoutsRequestEvent) {
        if (requestResponseEvent.response.body() != null) {
            val workouts = requestResponseEvent.response.body()!!.workouts.filter { it.favorite }
            adapter.setItems(workouts.toMutableList())
        } else {
            context.toast(R.string.error_occurred)
        }
    }

    override fun onWorkoutClicked(workout: Workout) {
        if (workout.type == WorkoutType.CUSTOM) {
            startActivity(context.intentFor<WorkoutCustomActivity>(NavigationConstants.KEY_WORKOUT to workout))
        } else {
            startActivity(context.intentFor<WorkoutActivity>(NavigationConstants.KEY_WORKOUT to workout))
        }
    }

}