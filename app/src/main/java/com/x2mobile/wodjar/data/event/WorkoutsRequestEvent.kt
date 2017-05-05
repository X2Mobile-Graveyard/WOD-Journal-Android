package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestResponseEvent
import com.x2mobile.wodjar.data.model.WorkoutsResponse
import retrofit2.Call
import retrofit2.Response

class WorkoutsRequestEvent(call: Call<WorkoutsResponse>?, response: Response<WorkoutsResponse>?) : RequestResponseEvent<WorkoutsResponse>(call, response)