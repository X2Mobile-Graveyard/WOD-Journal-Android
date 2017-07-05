package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestFailureEvent
import com.x2mobile.wodjar.data.model.PersonalRecordResult
import retrofit2.Call

class AddPersonalRecordResultRequestFailureEvent(call: Call<PersonalRecordResult>?, throwable: Throwable?) : RequestFailureEvent<PersonalRecordResult>(call, throwable)