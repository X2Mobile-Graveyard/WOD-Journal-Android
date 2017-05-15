package com.x2mobile.wodjar.data.model

import com.google.gson.annotations.SerializedName

class WorkoutsResponse {

    @SerializedName("wods", alternate = arrayOf("default_wods"))
    var workouts: List<Workout>? = null

}