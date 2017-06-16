package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestFailureEvent
import com.x2mobile.wodjar.data.model.Workout
import retrofit2.Call

class AddWorkoutRequestFailureEvent(call: Call<Workout>?, throwable: Throwable?) : RequestFailureEvent<Workout>(call, throwable)