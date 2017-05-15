package com.x2mobile.wodjar.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class PersonalRecord : Result, Parcelable {

    @SerializedName("name")
    var name: String? = null

    constructor() : super()

    constructor(personalRecordType: PersonalRecordType) : super() {
        name = personalRecordType.name
        type = personalRecordType.type
    }

    constructor(source: Parcel) : super(source) {
        name = source.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
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
