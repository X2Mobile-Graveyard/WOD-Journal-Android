package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestResponseEvent
import com.x2mobile.wodjar.data.model.WorkoutResultsResponse
import retrofit2.Call
import retrofit2.Response

class WorkoutResultsRequestEvent(call: Call<WorkoutResultsResponse>?, response: Response<WorkoutResultsResponse>) : RequestResponseEvent<WorkoutResultsResponse>(call, response)