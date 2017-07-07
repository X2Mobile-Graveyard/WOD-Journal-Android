package com.x2mobile.wodjar.ui.callback

import com.x2mobile.wodjar.data.model.base.BaseWorkout

interface WorkoutListener<in T : BaseWorkout> {
    fun onWorkoutClicked(workout: T)
}
