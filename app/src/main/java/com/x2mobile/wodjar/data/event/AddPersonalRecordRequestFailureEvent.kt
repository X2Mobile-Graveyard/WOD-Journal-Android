package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestFailureEvent
import com.x2mobile.wodjar.data.model.PersonalRecord
import retrofit2.Call

class AddPersonalRecordRequestFailureEvent(call: Call<PersonalRecord>?, throwable: Throwable?) : RequestFailureEvent<PersonalRecord>(call, throwable)