package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.callback.base.BaseCallback
import com.x2mobile.wodjar.data.event.AddPersonalRecordResultRequestEvent
import com.x2mobile.wodjar.data.event.AddPersonalRecordResultRequestFailureEvent
import com.x2mobile.wodjar.data.model.PersonalRecordResult
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

class AddPersonalRecordResultCallback : BaseCallback<PersonalRecordResult>() {

    override fun onFailure(call: Call<PersonalRecordResult>?, throwable: Throwable?) {
        EventBus.getDefault().post(AddPersonalRecordResultRequestFailureEvent(call, throwable))
    }

    override fun onSuccess(call: Call<PersonalRecordResult>?, response: Response<PersonalRecordResult>) {
        EventBus.getDefault().post(AddPersonalRecordResultRequestEvent(call, response))
    }

}