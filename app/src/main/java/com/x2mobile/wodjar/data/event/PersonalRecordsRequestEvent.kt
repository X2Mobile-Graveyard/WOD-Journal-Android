package com.x2mobile.wodjar.data.event

import com.x2mobile.wodjar.data.event.base.RequestResponseEvent
import com.x2mobile.wodjar.data.model.PersonalRecord
import retrofit2.Call
import retrofit2.Response

class PersonalRecordsRequestEvent(call: Call<List<PersonalRecord>>?, response: Response<List<PersonalRecord>>) : RequestResponseEvent<List<PersonalRecord>>(call, response)