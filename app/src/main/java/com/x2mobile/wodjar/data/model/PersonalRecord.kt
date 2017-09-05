package com.x2mobile.wodjar.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.x2mobile.wodjar.business.Constants
import com.x2mobile.wodjar.data.model.adapter.ResultTypeAdapter
import com.x2mobile.wodjar.data.model.base.Filterable
import java.util.*

class PersonalRecord() : Parcelable, Filterable {

    @SerializedName("id")
    var id: Int = Constants.ID_NA

    @SerializedName("name")
    var name: String? = null

    @SerializedName("result_type")
    @JsonAdapter(ResultTypeAdapter::class)
    var type: ResultType = ResultType.WEIGHT

    @SerializedName("present")
    var present: Boolean = false

    @SerializedName("best_result")
    var bestResult: Float = 0.0f

    @SerializedName("updated_at")
    private var updated: Date? = null

    constructor(name: String) : this(name, ResultType.WEIGHT)

    constructor(name: String, type: ResultType) : this() {
        this.name = name
        this.type = type
    }

    constructor(source: Parcel) : this() {
        id = source.readInt()
        name = source.readString()
        type = ResultType.values()[source.readInt()]
        present = source.readInt() == 0
        bestResult = source.readFloat()
        updated = source.readSerializable() as Date
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeInt(type.ordinal)
        dest.writeInt(if (present) 1 else 0)
        dest.writeFloat(bestResult)
        dest.writeSerializable(updated)
    }

    override fun describeContents(): Int = 0

    override fun matches(query: String): Boolean = name?.contains(query, true) == true

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PersonalRecord> = object : Parcelable.Creator<PersonalRecord> {
            override fun createFromParcel(source: Parcel): PersonalRecord = PersonalRecord(source)

            override fun newArray(size: Int): Array<PersonalRecord?> = arrayOfNulls(size)
        }
    }

}
