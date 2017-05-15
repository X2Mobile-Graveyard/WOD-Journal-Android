package com.x2mobile.wodjar.ui.callback

import com.x2mobile.wodjar.data.model.WorkoutResult

interface WorkoutResultListener {
    fun onWorkoutResultClicked(workoutResult: WorkoutResult)
}
