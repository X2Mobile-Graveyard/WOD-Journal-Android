package com.x2mobile.wodjar.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.x2mobile.wodjar.business.Constants

class WorkoutResult : Result, Parcelable {

    override var parentId: Int
        get() = workoutId
        set(value) {}

    @SerializedName("wod_id")
    var workoutId: Int = Constants.ID_NA

    @SerializedName("default")
    var default: Boolean = false

    constructor() : super()

    constructor(source: Parcel) : super(source) {
        workoutId = source.readInt()
        default = source.readInt() == 1
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeInt(workoutId)
        dest.writeInt(if (default) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<WorkoutResult> = object : Parcelable.Creator<WorkoutResult> {
            override fun createFromParcel(source: Parcel): WorkoutResult {
                return WorkoutResult(source)
            }

            override fun newArray(size: Int): Array<WorkoutResult?> {
                return arrayOfNulls(size)
            }
        }
    }

}