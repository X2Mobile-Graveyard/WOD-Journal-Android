package com.x2mobile.wodjar.data.model

import com.google.gson.annotations.SerializedName

class PersonalRecordsResponse {

    @SerializedName("personal_records")
    var personalRecords: List<PersonalRecord>? = null

}