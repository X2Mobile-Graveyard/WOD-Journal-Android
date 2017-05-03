package com.x2mobile.wodjar.data.model

import com.google.gson.annotations.SerializedName

class PersonalRecordTypesResponse {

    @SerializedName("personal_records")
    var personalRecordTypes: List<PersonalRecordType>? = null

}