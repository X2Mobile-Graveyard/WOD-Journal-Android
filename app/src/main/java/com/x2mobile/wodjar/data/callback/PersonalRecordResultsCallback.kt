package com.x2mobile.wodjar.data.callback

import com.x2mobile.wodjar.data.callback.base.BaseCallback
import com.x2mobile.wodjar.data.event.PersonalRecordResultsRequestEvent
import com.x2mobile.wodjar.data.event.PersonalRecordResultsRequestFailureEvent
import com.x2mobile.wodjar.data.model.PersonalRecordResult
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

class PersonalRecordResultsCallback : BaseCallback<List<PersonalRecordResult>>() {

    override fun onFailure(call: Call<List<PersonalRecordResult>>?, throwable: Throwable?) {
        EventBus.getDefault().post(PersonalRecordResultsRequestFailureEvent(call, throwable))
    }

    override fun onSuccess(call: Call<List<PersonalRecordResult>>?, response: Response<List<PersonalRecordResult>>) {
        EventBus.getDefault().post(PersonalRecordResultsRequestEvent(call, response))
    }

}