package com.x2mobile.wodjar.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.x2mobile.wodjar.business.Constants
import com.x2mobile.wodjar.data.model.adapter.ResultTypeAdapter
import com.x2mobile.wodjar.data.model.adapter.UriAdapter
import com.x2mobile.wodjar.data.model.adapter.WorkoutTypeAdapter
import com.x2mobile.wodjar.data.model.base.Filterable

class Workout() : Parcelable, Filterable {

    @SerializedName("id")
    var id: Int = Constants.ID_NA

    @SerializedName("name")
    var name: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("metric_description")
    var metricDescription: String? = null

    @SerializedName("history")
    var history: String? = null

    @SerializedName("wod_type")
    @JsonAdapter(WorkoutTypeAdapter::class)
    var type: WorkoutType = WorkoutType.CUSTOM

    @SerializedName("category")
    @JsonAdapter(ResultTypeAdapter::class)
    var resultType: ResultType = ResultType.WEIGHT

    @SerializedName("image")
    @JsonAdapter(UriAdapter::class)
    var imageUri: Uri? = null

    @SerializedName("video")
    var video: String? = null

    @SerializedName("best_result")
    var bestResult: Float = 0.0f

    @SerializedName("default")
    var default: Boolean = false

    constructor(source: Parcel) : this() {
        id = source.readInt()
        name = source.readString()
        description = source.readString()
        history = source.readString()
        type = WorkoutType.values()[source.readInt()]
        resultType = ResultType.values()[source.readInt()]
        imageUri = source.readParcelable(Workout::class.java.classLoader)
        video = source.readString()
        bestResult = source.readFloat()
        default = source.readInt() == 1
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeString(description)
        dest.writeString(history)
        dest.writeInt(type.ordinal)
        dest.writeInt(resultType.ordinal)
        dest.writeParcelable(imageUri, flags)
        dest.writeString(video)
        dest.writeFloat(bestResult)
        dest.writeInt(if (default) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun matches(query: String): Boolean {
        return name?.contains(query, true) ?: false
    }

    override fun equals(other: Any?): Boolean {
        return id == (other as? Workout)?.id ?: Constants.ID_NA
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (metricDescription?.hashCode() ?: 0)
        result = 31 * result + (history?.hashCode() ?: 0)
        result = 31 * result + type.hashCode()
        result = 31 * result + resultType.hashCode()
        result = 31 * result + (imageUri?.hashCode() ?: 0)
        result = 31 * result + (video?.hashCode() ?: 0)
        result = 31 * result + bestResult.hashCode()
        result = 31 * result + default.hashCode()
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