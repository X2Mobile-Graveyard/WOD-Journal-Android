package com.x2mobile.wodjar.data.model

import com.google.gson.annotations.SerializedName

class WorkoutResultsResponse {

    @SerializedName("wod_results")
    lateinit var workoutResults: List<WorkoutResult>

}