package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestFailureEvent
import retrofit2.Call

class DeletePersonalRecordResultsRequestFailureEvent(call: Call<Void>?, throwable: Throwable?) : RequestFailureEvent<Void>(call, throwable)