package com.x2mobile.wodjar.data.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.x2mobile.wodjar.business.Constants
import com.x2mobile.wodjar.data.model.adapter.FloatResultAdapter
import com.x2mobile.wodjar.data.model.adapter.IntResultAdapter
import com.x2mobile.wodjar.data.model.adapter.ResultTypeAdapter
import com.x2mobile.wodjar.data.model.adapter.UriAdapter
import java.util.*

abstract class Result() : BaseObservable(), Parcelable {

    @SerializedName("id")
    var id: Int = Constants.ID_NA

    abstract var parentId: Int

    @Bindable
    @SerializedName("rx")
    var rx: Boolean = false

    @SerializedName("result_weight")
    @JsonAdapter(FloatResultAdapter::class)
    var resultWeight: Float = 0f

    @Bindable
    @SerializedName("result_rounds")
    @JsonAdapter(IntResultAdapter::class)
    var resultReps: Int = 0

    @Bindable
    @SerializedName("result_time")
    @JsonAdapter(IntResultAdapter::class)
    var resultTime: Int = 0

    @SerializedName("result_type")
    @JsonAdapter(ResultTypeAdapter::class)
    var type: ResultType = ResultType.WEIGHT

    val result: Float
        get() = when (type) {
            ResultType.WEIGHT -> resultWeight
            ResultType.REPETITION -> resultReps.toFloat()
            ResultType.TIME -> resultTime.toFloat()
        }

    @Bindable
    @SerializedName("notes")
    var notes: String? = null

    @Bindable
    @SerializedName("image_url")
    @JsonAdapter(UriAdapter::class)
    var imageUri: Uri? = null

    @Bindable
    @SerializedName("date")
    var date: Date? = Date()

    constructor(source: Parcel) : this() {
        id = source.readInt()
        rx = source.readInt() == 1
        resultWeight = source.readFloat()
        resultReps = source.readInt()
        resultTime = source.readInt()
        val ordinal = source.readInt()
        type = ResultType.values()[ordinal]
        notes = source.readString()
        imageUri = source.readParcelable(javaClass.classLoader)
        date = source.readSerializable() as Date?
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeInt(if (rx) 1 else 0)
        dest.writeFloat(resultWeight)
        dest.writeInt(resultReps)
        dest.writeInt(resultTime)
        dest.writeInt(type.ordinal)
        dest.writeString(notes)
        dest.writeParcelable(imageUri, flags)
        dest.writeSerializable(date)
    }

    override fun describeContents(): Int = 0

    override fun equals(other: Any?): Boolean = if (other is Result) {
        id == other.id
    } else {
        false
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + rx.hashCode()
        result = 31 * result + resultWeight.hashCode()
        result = 31 * result + resultReps
        result = 31 * result + resultTime
        result = 31 * result + type.hashCode()
        result = 31 * result + (notes?.hashCode() ?: 0)
        result = 31 * result + (imageUri?.hashCode() ?: 0)
        result = 31 * result + (date?.hashCode() ?: 0)
        return result
    }

}

inline fun <T : Result, R : Comparable<R>> Iterable<T>.best(selector: (T) -> R): T? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    var bestElem = iterator.next()
    var bestValue = selector(bestElem)
    while (iterator.hasNext()) {
        val element = iterator.next()
        val value = selector(element)
        val better = when (element.type) {
            ResultType.TIME -> bestValue > value
            else -> bestValue < value
        }
        if (better) {
            bestElem = element
            bestValue = value
        }
    }
    return bestElem
}