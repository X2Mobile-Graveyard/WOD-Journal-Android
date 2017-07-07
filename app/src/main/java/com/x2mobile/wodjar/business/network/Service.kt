package com.x2mobile.wodjar.business.network

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import com.x2mobile.wodjar.BuildConfig
import com.x2mobile.wodjar.WodJarApplication
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.data.callback.*
import com.x2mobile.wodjar.data.model.*
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Service {

    private val ENDPOINT_URL = "https://wodjar-production.herokuapp.com/api/v1/"

    private val SIZE_OF_CACHE = 10L * 1024 * 1024 //10 MB

    private val api: Api by lazy {
        val okHttpBuilder = OkHttpClient.Builder()
                .addInterceptor(AuthorizationInterceptor(WodJarApplication.INSTANCE))
                .cache(Cache(WodJarApplication.INSTANCE.cacheDir, SIZE_OF_CACHE))
                .retryOnConnectionFailure(false)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            okHttpBuilder.addNetworkInterceptor(loggingInterceptor)
            okHttpBuilder.addNetworkInterceptor(StethoInterceptor())
        }

        Retrofit.Builder()
                .baseUrl(ENDPOINT_URL)
                .client(okHttpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
                .build().create(Api::class.java)
    }

    fun login(user: User, callback: Callback<LoginResponse> = LoginCallback()) {
        val call = api.login(UserBody(user))
        call.enqueue(callback)
    }

    fun login(accessToken: String, callback: Callback<LoginResponse> = LoginCallback()) {
        val call = api.login(accessToken)
        call.enqueue(callback)
    }

    fun signUp(user: User, callback: Callback<Void> = SignUpCallback()) {
        val call = api.signUp(UserBody(user))
        call.enqueue(callback)
    }

    fun updateUser(id: Int, user: User, callback: Callback<Void> = UpdateUserCallback()) {
        val call = api.updateUser(id, user)
        call.enqueue(callback)
    }

    fun getDefaultPersonalRecords(callback: Callback<List<PersonalRecord>> = PersonalRecordsCallback()) {
        val call = api.getDefaultPersonalRecords()
        call.enqueue(callback)
    }

    fun getPersonalRecords(callback: Callback<List<PersonalRecord>> = PersonalRecordsCallback(false)) {
        val call = api.getPersonalRecords()
        call.enqueue(callback)
    }

    fun updatePersonalRecord(personalRecord: PersonalRecord, callback: Callback<Void> = UpdatePersonalRecordCallback()) {
        val call = api.updatePersonalRecord(personalRecord.id, personalRecord)
        call.enqueue(callback)
    }

    fun deletePersonalRecord(id: Int, callback: Callback<Void> = DeletePersonalRecordCallback()) {
        val call = api.deletePersonalRecord(id)
        call.enqueue(callback)
    }

    fun getPersonalRecordResults(personalRecordId: Int, callback: Callback<List<PersonalRecordResult>> = PersonalRecordResultsCallback()) {
        val call = api.getPersonalRecordResults(personalRecordId)
        call.enqueue(callback)
    }

    fun savePersonalRecordResult(personalRecordName: String, personalRecordResult: PersonalRecordResult, callback: Callback<PersonalRecordResult> = AddPersonalRecordResultCallback()) {
        val call = api.savePersonalRecordResult(personalRecordName, personalRecordResult)
        call.enqueue(callback)
    }

    fun savePersonalRecordResult(personalRecordResult: PersonalRecordResult, callback: Callback<PersonalRecordResult> = AddPersonalRecordResultCallback()) {
        val call = api.savePersonalRecordResult(personalRecordResult.personalRecordId, personalRecordResult)
        call.enqueue(callback)
    }

    fun updatePersonalRecordResult(personalRecordResult: PersonalRecordResult, callback: Callback<Void> = UpdatePersonalRecordResultCallback()) {
        val call = api.updatePersonalRecordResult(personalRecordResult.id, personalRecordResult)
        call.enqueue(callback)
    }

    fun deletePersonalRecordResult(id: Int, callback: Callback<Void> = DeletePersonalRecordResultCallback()) {
        val call = api.deletePersonalRecordResult(id)
        call.enqueue(callback)
    }

    fun getDefaultWorkouts(workoutType: WorkoutType, callback: Callback<MutableList<Workout>> = WorkoutsCallback(workoutType)) {
        val call = api.getDefaultWorkouts(workoutType.ordinal)
        call.enqueue(callback)
    }

    fun getDefaultWorkout(id: Int, workoutType: WorkoutType, callback: Callback<Workout> = WorkoutCallback()) {
        val call = api.getDefaultWorkout(id, workoutType.ordinal)
        call.enqueue(callback)
    }

    fun getWorkouts(workoutType: WorkoutType, callback: Callback<MutableList<Workout>> = WorkoutsCallback(workoutType)) {
        val call = api.getWorkouts(workoutType.ordinal)
        call.enqueue(callback)
    }

    fun getWorkoutsCustom(callback: Callback<MutableList<WorkoutCustom>> = WorkoutsCustomCallback()) {
        val call = api.getWorkoutsCustom()
        call.enqueue(callback)
    }

    fun getWorkout(id: Int, workoutType: WorkoutType, callback: Callback<Workout> = WorkoutCallback()) {
        val call = api.getWorkout(id, workoutType.ordinal)
        call.enqueue(callback)
    }

    fun getWorkoutCustom(id: Int, callback: Callback<WorkoutCustom> = WorkoutCustomCallback()) {
        val call = api.getWorkoutCustom(id)
        call.enqueue(callback)
    }

    fun saveWorkout(workout: WorkoutCustom, callback: Callback<WorkoutCustom> = AddWorkoutCallback()) {
        val call = api.saveWorkout(workout)
        call.enqueue(callback)
    }

    fun updateWorkout(workout: WorkoutCustom, callback: Callback<Void> = UpdateWorkoutCallback()) {
        val call = api.updateWorkout(workout.id, workout)
        call.enqueue(callback)
    }

    fun deleteWorkout(workoutId: Int, callback: Callback<Void> = DeleteWorkoutCallback()) {
        val call = api.deleteWorkout(workoutId)
        call.enqueue(callback)
    }

    fun getWorkoutResults(workoutId: Int, callback: Callback<WorkoutResultsResponse> = WorkoutResultsCallback()) {
        val call = api.getWorkoutResults(workoutId)
        call.enqueue(callback)
    }

    fun saveWorkoutResult(workoutResult: WorkoutResult, callback: Callback<WorkoutResult> = AddWorkoutResultCallback()) {
        val call = api.saveWorkoutResult(workoutResult)
        call.enqueue(callback)
    }

    fun updateWorkoutResult(workoutResult: WorkoutResult, callback: Callback<Void> = UpdateWorkoutResultCallback()) {
        val call = api.updateWorkoutResult(workoutResult.id, workoutResult)
        call.enqueue(callback)
    }

    fun deleteWorkoutResult(id: Int, callback: Callback<Void> = DeleteWorkoutResultCallback()) {
        val call = api.deleteWorkoutResult(id)
        call.enqueue(callback)
    }

    private class AuthorizationInterceptor(val context: Context) : Interceptor {

        private val AUTHORIZATION_HEADER = "Authorization"
        private val AUTHORIZATION_VALUE = "Token %s"

        private val API_KEY_HEADER = "X-Api-Key"
        private val API_KEY_VALUE = "zz&Ci9XK7Wm8WrWXdT^jAiAmS4OT9mMNDB101Sye*rbrGUPUxj*Q1Hpk@I1i%t7F"

        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = chain.request().newBuilder()
            if (Preference.isLoggedIn(context)) {
                requestBuilder.addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_VALUE.format(Preference.getToken(context)))
            } else {
                requestBuilder.addHeader(API_KEY_HEADER, API_KEY_VALUE)
            }
            return chain.proceed(requestBuilder.build())
        }

    }
}