package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestResponseEvent
import com.x2mobile.wodjar.data.model.WorkoutCustom
import retrofit2.Call
import retrofit2.Response

class WorkoutsCustomRequestEvent(call: Call<MutableList<WorkoutCustom>>?, response: Response<MutableList<WorkoutCustom>>) :
        RequestResponseEvent<MutableList<WorkoutCustom>>(call, response)