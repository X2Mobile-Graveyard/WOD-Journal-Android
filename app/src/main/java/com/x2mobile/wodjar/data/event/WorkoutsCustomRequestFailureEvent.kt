package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestFailureEvent
import com.x2mobile.wodjar.data.model.WorkoutCustom
import retrofit2.Call

class WorkoutsCustomRequestFailureEvent(call: Call<MutableList<WorkoutCustom>>?, throwable: Throwable?) : RequestFailureEvent<MutableList<WorkoutCustom>>(call, throwable)