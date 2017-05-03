package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestFailureEvent
import com.x2mobile.wodjar.data.model.PersonalRecordTypesResponse
import retrofit2.Call

class PersonalRecordTypesRequestFailureEvent(call: Call<PersonalRecordTypesResponse>?, throwable: Throwable?) : RequestFailureEvent<PersonalRecordTypesResponse>(call, throwable)