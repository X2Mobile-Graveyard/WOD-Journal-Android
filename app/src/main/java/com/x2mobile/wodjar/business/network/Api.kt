package com.x2mobile.wodjar.business.network

import com.x2mobile.wodjar.data.model.*
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @POST("sign-in")
    fun login(@Body body: UserBody): Call<LoginResponse>

    @GET("facebook-sign-in")
    fun login(@Query("access_token") accessToken: String): Call<LoginResponse>

    @POST("users")
    fun signUp(@Body body: UserBody): Call<Void>

    @GET("list-prs")
    fun getPersonalRecordTypes(): Call<PersonalRecordTypesResponse>

    @GET("list-prs-by-name/{name}")
    fun getPersonalRecords(@Path("name") name: String): Call<PersonalRecordsResponse>

    @POST("personal_records")
    fun savePersonalRecord(@Body personalRecord: PersonalRecord): Call<PersonalRecord>

    @PATCH("personal_records/{id}")
    fun updatePersonalRecord(@Body personalRecord: PersonalRecord, @Path("id") id: Int = personalRecord.id): Call<Void>

    @DELETE("personal_records/{id}")
    fun deletePersonalRecord(@Path("id") id: Int): Call<Void>

    @PUT("update-prs/{name}")
    fun updatePersonalRecords(@Body ids: List<Int>, @Path("name") name: String): Call<Void>

    @DELETE("delete-prs-by-name/{name}")
    fun deletePersonalRecords(@Path("name") name: String): Call<Void>
}