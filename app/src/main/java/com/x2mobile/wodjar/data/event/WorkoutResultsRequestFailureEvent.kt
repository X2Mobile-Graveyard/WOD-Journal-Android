package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestFailureEvent
import com.x2mobile.wodjar.data.model.WorkoutResultsResponse
import retrofit2.Call

class WorkoutResultsRequestFailureEvent(call: Call<WorkoutResultsResponse>?, throwable: Throwable?) : RequestFailureEvent<WorkoutResultsResponse>(call, throwable)