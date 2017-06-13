package com.x2mobile.wodjar.data.model

import com.google.gson.annotations.SerializedName

class WorkoutsResponse {

    @SerializedName("wods", alternate = arrayOf("default_wods"))
    lateinit var workouts: List<Workout>

}