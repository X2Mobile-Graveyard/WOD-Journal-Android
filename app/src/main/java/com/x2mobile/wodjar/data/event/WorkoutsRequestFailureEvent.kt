package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestFailureEvent
import com.x2mobile.wodjar.data.model.WorkoutsResponse
import retrofit2.Call

class WorkoutsRequestFailureEvent(call: Call<WorkoutsResponse>?, throwable: Throwable?) : RequestFailureEvent<WorkoutsResponse>(call, throwable)