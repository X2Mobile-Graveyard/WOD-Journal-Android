package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestResponseEvent
import com.x2mobile.wodjar.data.model.Workout
import retrofit2.Call
import retrofit2.Response

class AddWorkoutRequestEvent(call: Call<Workout>?, response: Response<Workout>) : RequestResponseEvent<Workout>(call, response)