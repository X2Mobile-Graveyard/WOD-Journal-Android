package com.x2mobile.wodjar.data.model

import com.google.gson.annotations.SerializedName
import com.x2mobile.wodjar.business.Constants

class LoginResponse {

    @SerializedName("user_id")
    var userId: Int = Constants.ID_NA

    @SerializedName("auth_token")
    var authToken: String? = null

    @SerializedName("errors")
    var errors: List<String>? = null

}