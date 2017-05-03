package com.x2mobile.wodjar.data.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.x2mobile.wodjar.business.Constants
import com.x2mobile.wodjar.data.model.adapter.PersonalRecordCategoryAdapter
import com.x2mobile.wodjar.data.model.adapter.UriAdapter
import java.util.*

class PersonalRecord() : BaseObservable(), Parcelable {

    @SerializedName("id")
    var id: Int = Constants.ID_NA

    @SerializedName("name")
    var name: String? = null

    @Bindable
    @SerializedName("rx")
    var rx: Boolean = false

    @Bindable
    @SerializedName("result_weight")
    var resultWeight: Float = 0f

    @Bindable
    @SerializedName("result_rounds")
    var resultReps: Int = 0

    @Bindable
    @SerializedName("result_time")
    var resultTime: Int = 0

    @SerializedName("result_type")
    @JsonAdapter(PersonalRecordCategoryAdapter::class)
    var category: PersonalRecordCategory = PersonalRecordCategory.OTHER

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

    constructor(personalRecordType: PersonalRecordType) : this() {
        name = personalRecordType.name
        category = personalRecordType.category
    }

    constructor(source: Parcel) : this() {
        id = source.readInt();
        name = source.readString()
        rx = source.readInt() == 1
        resultWeight = source.readFloat()
        resultReps = source.readInt()
        resultTime = source.readInt()
        val ordinal = source.readInt()
        category = PersonalRecordCategory.values()[ordinal]
        notes = source.readString()
        imageUri = source.readParcelable(javaClass.classLoader)
        date = source.readSerializable() as Date?
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeString(name)
        dest?.writeInt(if (rx) 1 else 0)
        dest?.writeFloat(resultWeight)
        dest?.writeInt(resultReps)
        dest?.writeInt(resultTime)
        dest?.writeInt(category.ordinal)
        dest?.writeString(notes)
        dest?.writeParcelable(imageUri, flags)
        dest?.writeSerializable(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (other is PersonalRecord) {
            return id == other.id
        } else {
            return false
        }
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PersonalRecord> = object : Parcelable.Creator<PersonalRecord> {
            override fun createFromParcel(source: Parcel): PersonalRecord {
                return PersonalRecord(source)
            }

            override fun newArray(size: Int): Array<PersonalRecord?> {
                return arrayOfNulls(size)
            }
        }
    }
}
