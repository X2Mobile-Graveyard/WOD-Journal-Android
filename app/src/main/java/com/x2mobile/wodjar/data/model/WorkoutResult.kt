package com.x2mobile.wodjar.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.x2mobile.wodjar.business.Constants

class WorkoutResult : Result(), Parcelable {

    @SerializedName("wod_id")
    var workoutId: Int = Constants.ID_NA

    @SerializedName("default")
    var default: Boolean = false

}