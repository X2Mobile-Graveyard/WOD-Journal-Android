package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.event.UpdatePersonalRecordRequestEvent
import com.x2mobile.wodjar.data.event.UpdatePersonalRecordRequestFailureEvent
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdatePersonalRecordCallback : Callback<Void> {

    override fun onFailure(call: Call<Void>?, throwable: Throwable?) {
        EventBus.getDefault().post(UpdatePersonalRecordRequestFailureEvent(call, throwable))
    }

    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
        EventBus.getDefault().post(UpdatePersonalRecordRequestEvent(call, response))
    }

}