package com.x2mobile.wodjar.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.x2mobile.wodjar.business.Constants
import com.x2mobile.wodjar.data.model.adapter.ResultTypeAdapter
import com.x2mobile.wodjar.data.model.adapter.WorkoutTypeAdapter

class Workout() : Parcelable {

    @SerializedName("id")
    var id: Int = Constants.ID_NA

    @SerializedName("name")
    var name: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("history")
    var history: String? = null

    @SerializedName("wod_type")
    @JsonAdapter(WorkoutTypeAdapter::class)
    var type: WorkoutType = WorkoutType.CUSTOM

    @SerializedName("category")
    @JsonAdapter(ResultTypeAdapter::class)
    var resultType: ResultType = ResultType.OTHER

    @SerializedName("image")
    var image: String? = null

    @SerializedName("video")
    var video: String? = null

    @SerializedName("favorites")
    var favorite: Boolean = false

    @SerializedName("completed")
    var completed: Boolean = false

    @SerializedName("default")
    var default: Boolean = false

    constructor(source: Parcel) : this() {
        id = source.readInt()
        name = source.readString()
        description = source.readString()
        history = source.readString()
        type = WorkoutType.values()[source.readInt()]
        resultType = ResultType.values()[source.readInt()]
        image = source.readString()
        video = source.readString()
        favorite = source.readInt() == 1
        completed = source.readInt() == 1
        default = source.readInt() == 1
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeString(description)
        dest.writeString(history)
        dest.writeInt(type.ordinal)
        dest.writeInt(resultType.ordinal)
        dest.writeString(image)
        dest.writeString(video)
        dest.writeInt(if (favorite) 1 else 0)
        dest.writeInt(if (completed) 1 else 0)
        dest.writeInt(if (default) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        return id == (other as? Workout)?.id ?: Constants.ID_NA
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (history?.hashCode() ?: 0)
        result = 31 * result + type.hashCode()
        result = 31 * result + resultType.hashCode()
        result = 31 * result + (image?.hashCode() ?: 0)
        result = 31 * result + (video?.hashCode() ?: 0)
        result = 31 * result + favorite.hashCode()
        result = 31 * result + completed.hashCode()
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