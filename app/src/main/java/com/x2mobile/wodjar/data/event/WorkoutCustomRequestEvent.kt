package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestResponseEvent
import com.x2mobile.wodjar.data.model.WorkoutCustom
import retrofit2.Call
import retrofit2.Response

class WorkoutCustomRequestEvent(call: Call<WorkoutCustom>?, response: Response<WorkoutCustom>) : RequestResponseEvent<WorkoutCustom>(call, response)