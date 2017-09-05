package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.callback.base.BaseCallback
import com.x2mobile.wodjar.data.event.WorkoutResultsRequestEvent
import com.x2mobile.wodjar.data.event.WorkoutResultsRequestFailureEvent
import com.x2mobile.wodjar.data.model.WorkoutResultsResponse
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

class WorkoutResultsCallback : BaseCallback<WorkoutResultsResponse>() {

    override fun onFailure(call: Call<WorkoutResultsResponse>?, throwable: Throwable?) = EventBus.getDefault().post(WorkoutResultsRequestFailureEvent(call, throwable))

    override fun onSuccess(call: Call<WorkoutResultsResponse>?, response: Response<WorkoutResultsResponse>) = EventBus.getDefault().post(WorkoutResultsRequestEvent(call, response))

}