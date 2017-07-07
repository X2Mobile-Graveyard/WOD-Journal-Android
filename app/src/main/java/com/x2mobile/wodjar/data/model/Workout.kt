package com.x2mobile.wodjar.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.x2mobile.wodjar.data.model.base.BaseWorkout
import com.x2mobile.wodjar.data.model.base.Filterable

class Workout : BaseWorkout, Filterable {

    @SerializedName("name")
    var name: String? = null

    constructor() : super()

    constructor(source: Parcel) : super(source) {
        name = source.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(name)
    }

    override fun matches(query: String): Boolean {
        return name?.contains(query, true) ?: false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        return result
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Workout> = object : Parcelable.Creator<Workout> {
            override fun createFromParcel(source: Parcel): Workout {
                return Workout(source)
            }

            override fun newArray(size: Int): Array<Workout?> {
                return arrayOfNulls(size)
            }
        }
    }
}