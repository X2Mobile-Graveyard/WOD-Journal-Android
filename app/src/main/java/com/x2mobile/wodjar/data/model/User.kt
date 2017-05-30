package com.x2mobile.wodjar.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.x2mobile.wodjar.data.model.adapter.UriAdapter

data class User(val email: String, val password: String? = null, val name: String? = null,
                @SerializedName("image_url") @JsonAdapter(UriAdapter::class) var imageUri: Uri? = null) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString(), parcel.readString(), parcel.readString(),
            parcel.readParcelable(User::class.java.classLoader))

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(email)
        dest.writeString(password)
        dest.writeString(name)
        dest.writeParcelable(imageUri, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User {
                return User(source)
            }

            override fun newArray(size: Int): Array<User?> {
                return arrayOfNulls(size)
            }
        }
    }
}