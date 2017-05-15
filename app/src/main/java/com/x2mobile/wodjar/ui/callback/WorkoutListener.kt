package com.x2mobile.wodjar.ui.callback

import com.x2mobile.wodjar.data.model.Workout

interface WorkoutListener {
    fun onWorkoutClicked(workout: Workout)
}
