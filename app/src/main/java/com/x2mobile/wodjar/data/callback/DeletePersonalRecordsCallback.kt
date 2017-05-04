package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.event.DeletePersonalRecordsRequestEvent
import com.x2mobile.wodjar.data.event.DeletePersonalRecordsRequestFailureEvent
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeletePersonalRecordsCallback : Callback<Void> {

    override fun onFailure(call: Call<Void>?, throwable: Throwable?) {
        EventBus.getDefault().post(DeletePersonalRecordsRequestFailureEvent(call, throwable))
    }

    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
        EventBus.getDefault().post(DeletePersonalRecordsRequestEvent(call, response))
    }

}