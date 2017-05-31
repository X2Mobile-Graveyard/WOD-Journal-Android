package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestResponseEvent
import com.x2mobile.wodjar.data.model.WorkoutResult
import retrofit2.Call
import retrofit2.Response

class AddWorkoutResultRequestEvent(call: Call<WorkoutResult>?, response: Response<WorkoutResult>?) : RequestResponseEvent<WorkoutResult>(call, response)