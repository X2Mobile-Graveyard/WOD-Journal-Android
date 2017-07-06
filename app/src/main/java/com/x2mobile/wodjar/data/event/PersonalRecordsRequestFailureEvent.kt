package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestFailureEvent
import com.x2mobile.wodjar.data.model.PersonalRecord
import retrofit2.Call

class PersonalRecordsRequestFailureEvent(call: Call<List<PersonalRecord>>?, throwable: Throwable?, val default: Boolean) : RequestFailureEvent<List<PersonalRecord>>(call, throwable)