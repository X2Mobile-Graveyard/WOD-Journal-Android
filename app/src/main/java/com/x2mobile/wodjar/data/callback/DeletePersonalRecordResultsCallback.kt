package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.callback.base.BaseCallback
import com.x2mobile.wodjar.data.event.DeletePersonalRecordResultsRequestEvent
import com.x2mobile.wodjar.data.event.DeletePersonalRecordResultsRequestFailureEvent
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

class DeletePersonalRecordResultsCallback : BaseCallback<Void>() {

    override fun onFailure(call: Call<Void>?, throwable: Throwable?) {
        EventBus.getDefault().post(DeletePersonalRecordResultsRequestFailureEvent(call, throwable))
    }

    override fun onSuccess(call: Call<Void>?, response: Response<Void>) {
        EventBus.getDefault().post(DeletePersonalRecordResultsRequestEvent(call, response))
    }

}