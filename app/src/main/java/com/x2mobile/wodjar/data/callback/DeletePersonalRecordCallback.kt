package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.callback.base.BaseCallback
import com.x2mobile.wodjar.data.event.DeletePersonalRecordRequestEvent
import com.x2mobile.wodjar.data.event.DeletePersonalRecordRequestFailureEvent
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

class DeletePersonalRecordCallback : BaseCallback<Void>() {

    override fun onFailure(call: Call<Void>?, throwable: Throwable?) = EventBus.getDefault().post(DeletePersonalRecordRequestFailureEvent(call, throwable))

    override fun onSuccess(call: Call<Void>?, response: Response<Void>) = EventBus.getDefault().post(DeletePersonalRecordRequestEvent(call, response))

}