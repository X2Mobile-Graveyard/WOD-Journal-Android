package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestFailureEvent
import com.x2mobile.wodjar.data.model.PersonalRecordsResponse
import retrofit2.Call

class PersonalRecordsRequestFailureEvent(call: Call<PersonalRecordsResponse>?, throwable: Throwable?) : RequestFailureEvent<PersonalRecordsResponse>(call, throwable)