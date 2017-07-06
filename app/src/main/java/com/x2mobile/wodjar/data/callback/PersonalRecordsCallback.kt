package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.callback.base.BaseCallback
import com.x2mobile.wodjar.data.event.PersonalRecordsRequestEvent
import com.x2mobile.wodjar.data.event.PersonalRecordsRequestFailureEvent
import com.x2mobile.wodjar.data.model.PersonalRecord
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

class PersonalRecordsCallback(val default: Boolean = true) : BaseCallback<List<PersonalRecord>>() {

    override fun onFailure(call: Call<List<PersonalRecord>>?, throwable: Throwable?) {
        EventBus.getDefault().post(PersonalRecordsRequestFailureEvent(call, throwable, default))
    }

    override fun onSuccess(call: Call<List<PersonalRecord>>?, response: Response<List<PersonalRecord>>) {
        EventBus.getDefault().postSticky(PersonalRecordsRequestEvent(call, response))
    }

}