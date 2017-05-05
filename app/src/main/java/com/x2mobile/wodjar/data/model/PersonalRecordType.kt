package com.x2mobile.wodjar.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.x2mobile.wodjar.data.model.adapter.PersonalRecordCategoryAdapter
import com.x2mobile.wodjar.data.model.base.Filterable
import java.util.*

class PersonalRecordType() : Parcelable, Filterable {

    @SerializedName("name")
    var name: String? = null

    @SerializedName("result_type")
    @JsonAdapter(PersonalRecordCategoryAdapter::class)
    var category: PersonalRecordCategory = PersonalRecordCategory.OTHER

    @SerializedName("updated_at")
    var updated: Date? = null

    @SerializedName("present")
    var present: Boolean = false

    constructor(name: String, category: PersonalRecordCategory) : this() {
        this.name = name
        this.category = category
    }

    constructor(source: Parcel) : this() {
        name = source.readString()
        val ordinal = source.readInt()
        category = PersonalRecordCategory.values()[ordinal]
        present = source.readInt() == 1
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeInt(category.ordinal)
        dest?.writeInt(if (present) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun matches(query: String): Boolean {
        return name?.contains(query) ?: false
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PersonalRecordType> = object : Parcelable.Creator<PersonalRecordType> {
            override fun createFromParcel(source: Parcel): PersonalRecordType {
                return PersonalRecordType(source)
            }

            override fun newArray(size: Int): Array<PersonalRecordType?> {
                return arrayOfNulls(size)
            }
        }
    }

}
