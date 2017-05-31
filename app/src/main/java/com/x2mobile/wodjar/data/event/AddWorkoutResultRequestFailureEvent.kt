package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestFailureEvent
import com.x2mobile.wodjar.data.model.WorkoutResult
import retrofit2.Call

class AddWorkoutResultRequestFailureEvent(call: Call<WorkoutResult>?, throwable: Throwable?) : RequestFailureEvent<WorkoutResult>(call, throwable)