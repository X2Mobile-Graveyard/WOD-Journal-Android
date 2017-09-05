package com.x2mobile.wodjar.fragments

import com.x2mobile.wodjar.data.event.WorkoutOpensRequestEvent
import com.x2mobile.wodjar.data.model.Workout
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class WorkoutOpensListFragment : WorkoutListFragment() {

    override fun sortWorkouts(workouts: List<Workout>): List<Workout> = super.sortWorkouts(workouts).asReversed()

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onWorkoutsResponse(requestResponseEvent: WorkoutOpensRequestEvent) = handleWorkoutsResponse(requestResponseEvent)
}
