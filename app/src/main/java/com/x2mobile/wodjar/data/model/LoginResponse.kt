package com.x2mobile.wodjar.data.model

import android.net.Uri
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.x2mobile.wodjar.business.Constants
import com.x2mobile.wodjar.data.model.adapter.UriAdapter

class LoginResponse {

    @SerializedName("user_id")
    var userId: Int = Constants.ID_NA

    @SerializedName("user_email")
    var email: String? = null

    @SerializedName("user_name")
    var name: String? = null

    @SerializedName("user_image_url")
    @JsonAdapter(UriAdapter::class)
    var imageUri: Uri? = null

    @SerializedName("auth_token")
    var authToken: String? = null

}