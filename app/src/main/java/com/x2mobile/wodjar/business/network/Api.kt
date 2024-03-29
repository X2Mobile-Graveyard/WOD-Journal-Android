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

    @GET("default-prs")
    fun getDefaultPersonalRecords(): Call<List<PersonalRecord>>

    @GET("list-prs")
    fun getPersonalRecords(): Call<List<PersonalRecord>>

    @PATCH("personal_records/{id}")
    fun updatePersonalRecord(@Path("id") id: Int, @Body personalRecord: PersonalRecord): Call<Void>

    @DELETE("personal_records/{id}")
    fun deletePersonalRecord(@Path("id") id: Int): Call<Void>

    @GET("pr-results/{pr_id}")
    fun getPersonalRecordResults(@Path("pr_id") personalRecordId: Int): Call<List<PersonalRecordResult>>

    @POST("create-pr-result/{pr_name}")
    fun savePersonalRecordResult(@Path("pr_name") personalRecordName: String, @Body personalRecordResult: PersonalRecordResult): Call<PersonalRecordResult>

    @POST("pr-results/{pr_id}")
    fun savePersonalRecordResult(@Path("pr_id") personalRecordId: Int, @Body personalRecordResult: PersonalRecordResult): Call<PersonalRecordResult>

    @PATCH("pr_results/{id}")
    fun updatePersonalRecordResult(@Path("id") id: Int, @Body personalRecordResult: PersonalRecordResult): Call<Void>

    @DELETE("pr_results/{id}")
    fun deletePersonalRecordResult(@Path("id") id: Int): Call<Void>

    @GET("list-default-wods/{workoutType}")
    fun getDefaultWorkouts(@Path("workoutType") workoutType: Int): Call<MutableList<Workout>>

    @GET("default-wods/{id}/{workoutType}")
    fun getDefaultWorkout(@Path("id") id: Int, @Path("workoutType") workoutType: Int): Call<Workout>

    @GET("list-wods/{workoutType}")
    fun getWorkouts(@Path("workoutType") workoutType: Int): Call<MutableList<Workout>>

    @GET("list-custom-wods")
    fun getWorkoutsCustom(): Call<MutableList<WorkoutCustom>>

    @GET("wods/{id}/{workoutType}")
    fun getWorkout(@Path("id") id: Int, @Path("workoutType") workoutType: Int): Call<Workout>

    @GET("custom-wods/{id}")
    fun getWorkoutCustom(@Path("id") id: Int): Call<WorkoutCustom>

    @POST("wods")
    fun saveWorkout(@Body workout: WorkoutCustom): Call<WorkoutCustom>

    @PATCH("wods/{wod_id}")
    fun updateWorkout(@Path("wod_id") workoutId: Int, @Body workout: WorkoutCustom): Call<Void>

    @DELETE("wods/{wod_id}")
    fun deleteWorkout(@Path("wod_id") workoutId: Int): Call<Void>

    @GET("list-wrs-by-wod/{wod_id}")
    fun getWorkoutResults(@Path("wod_id") workoutId: Int): Call<WorkoutResultsResponse>

    @POST("wod_results")
    fun saveWorkoutResult(@Body workoutResult: WorkoutResult): Call<WorkoutResult>

    @PATCH("wod_results/{id}")
    fun updateWorkoutResult(@Path("id") id: Int, @Body workoutResult: WorkoutResult): Call<Void>

    @DELETE("wod_results/{id}")
    fun deleteWorkoutResult(@Path("id") id: Int): Call<Void>
}