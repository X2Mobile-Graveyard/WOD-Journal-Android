package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestFailureEvent
import com.x2mobile.wodjar.data.model.PersonalRecordResult
import retrofit2.Call

class PersonalRecordResultsRequestFailureEvent(call: Call<List<PersonalRecordResult>>?, throwable: Throwable?) : RequestFailureEvent<List<PersonalRecordResult>>(call, throwable)