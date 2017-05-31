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

    @PATCH("users/{id}")
    fun updateUser(@Path("id") id: Int, @Body user: User): Call<Void>

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

    @HTTP(method = "DELETE", path = "delete-prs", hasBody = true)
    fun deletePersonalRecords(@Body ids: List<Int>): Call<Void>

    @PUT("update-prs/{name}")
    fun updatePersonalRecords(@Body ids: List<Int>, @Path("name") name: String): Call<Void>

    @DELETE("delete-prs-by-name/{name}")
    fun deletePersonalRecords(@Path("name") name: String): Call<Void>

    @GET("default-wods")
    fun getDefaultWorkouts(): Call<WorkoutsResponse>

    @GET("list-wods")
    fun getWorkouts(): Call<WorkoutsResponse>

    @POST("wods/favorite/{wod_id}")
    fun updateWorkout(@Path("wod_id") workoutId: Int, @Query("default") default: Boolean, @Query("favorite") favorite: Boolean): Call<Void>

    @GET("list-wrs-by-wod/{wod_id}")
    fun getWorkoutResults(@Path("wod_id") workoutId: Int): Call<WorkoutResultsResponse>

    @POST("wod_results")
    fun saveWorkoutResult(@Body workoutResult: WorkoutResult): Call<WorkoutResult>

    @PATCH("wod_results/{id}")
    fun updateWorkoutResult(@Path("id") id: Int, @Body workoutResult: WorkoutResult): Call<Void>

    @DELETE("wod_results/{id}")
    fun deleteWorkoutResult(@Path("id") id: Int): Call<Void>
}