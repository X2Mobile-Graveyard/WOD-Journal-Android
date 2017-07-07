package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestFailureEvent
import com.x2mobile.wodjar.data.model.WorkoutCustom
import retrofit2.Call

class WorkoutCustomRequestFailureEvent(call: Call<WorkoutCustom>?, throwable: Throwable?) : RequestFailureEvent<WorkoutCustom>(call, throwable)