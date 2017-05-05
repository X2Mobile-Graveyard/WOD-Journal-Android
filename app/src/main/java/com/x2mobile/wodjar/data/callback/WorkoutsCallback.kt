package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.event.WorkoutsRequestEvent
import com.x2mobile.wodjar.data.event.WorkoutsRequestFailureEvent
import com.x2mobile.wodjar.data.model.WorkoutsResponse
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutsCallback : Callback<WorkoutsResponse> {

    override fun onFailure(call: Call<WorkoutsResponse>?, throwable: Throwable?) {
        EventBus.getDefault().post(WorkoutsRequestFailureEvent(call, throwable))
    }

    override fun onResponse(call: Call<WorkoutsResponse>?, response: Response<WorkoutsResponse>?) {
        EventBus.getDefault().postSticky(WorkoutsRequestEvent(call, response))
    }

}