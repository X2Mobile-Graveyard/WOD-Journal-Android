package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestResponseEvent
import retrofit2.Call
import retrofit2.Response

class UpdateWorkoutResultRequestEvent(call: Call<Void>?, response: Response<Void>?) : RequestResponseEvent<Void>(call, response)