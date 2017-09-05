package com.x2mobile.wodjar.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.x2mobile.wodjar.data.model.base.BaseWorkout
import java.util.*

class WorkoutCustom : BaseWorkout {

    @SerializedName("date")
    var date: Date = Date()

    constructor() : super()

    constructor(source: Parcel) : super(source) {
        date = source.readSerializable() as Date
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeSerializable(date)
    }

    override fun matches(query: String): Boolean = true

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<WorkoutCustom> = object : Parcelable.Creator<WorkoutCustom> {
            override fun createFromParcel(source: Parcel): WorkoutCustom = WorkoutCustom(source)

            override fun newArray(size: Int): Array<WorkoutCustom?> = arrayOfNulls(size)
        }
    }
}
