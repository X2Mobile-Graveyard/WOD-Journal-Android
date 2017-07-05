package com.x2mobile.wodjar.data.model

import android.os.Parcel
import android.os.Parcelable
import com.x2mobile.wodjar.business.Constants

class PersonalRecordResult : Result, Parcelable {

    var personalRecordId: Int = Constants.ID_NA

    constructor() : super()

    constructor(source: Parcel) : super(source) {
        personalRecordId = source.readInt()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeInt(personalRecordId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PersonalRecordResult> = object : Parcelable.Creator<PersonalRecordResult> {
            override fun createFromParcel(source: Parcel): PersonalRecordResult {
                return PersonalRecordResult(source)
            }

            override fun newArray(size: Int): Array<PersonalRecordResult?> {
                return arrayOfNulls(size)
            }
        }
    }
}
