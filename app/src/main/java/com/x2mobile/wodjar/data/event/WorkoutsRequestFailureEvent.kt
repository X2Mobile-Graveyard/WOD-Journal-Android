package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestFailureEvent
import com.x2mobile.wodjar.data.model.Workout
import retrofit2.Call

class WorkoutsRequestFailureEvent(call: Call<MutableList<Workout>>?, throwable: Throwable?) : RequestFailureEvent<MutableList<Workout>>(call, throwable)