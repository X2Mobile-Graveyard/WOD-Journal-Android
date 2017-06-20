package com.x2mobile.wodjar.fragments

import com.x2mobile.wodjar.data.model.Workout

class WorkoutOpensListFragment : WorkoutListFragment() {

    override fun filterWorkouts(workouts: List<Workout>): List<Workout> {
        return super.filterWorkouts(workouts).asReversed()
    }
}
