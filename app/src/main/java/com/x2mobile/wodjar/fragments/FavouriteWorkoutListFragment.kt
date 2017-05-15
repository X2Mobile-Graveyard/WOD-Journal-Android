package com.x2mobile.wodjar.fragments

import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.data.event.WorkoutFavouriteToggle
import com.x2mobile.wodjar.data.event.WorkoutsRequestEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast

class FavouriteWorkoutListFragment : WorkoutListFragment() {

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutsResponse(event: WorkoutFavouriteToggle) {
        val workout = event.workout
        if (event.workout.favorite) {
            adapter.addItem(workout, 0)
        } else {
            adapter.removeItem(workout)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    override fun onWorkoutsResponse(requestResponseEvent: WorkoutsRequestEvent) {
        if (requestResponseEvent.response != null && requestResponseEvent.response.isSuccessful &&
                requestResponseEvent.response.body() != null) {
            val workouts = requestResponseEvent.response.body().workouts!!.filter { it.favorite }
            adapter.setItems(workouts.toMutableList())
        } else {
            context.toast(R.string.error_occurred)
        }
    }

}