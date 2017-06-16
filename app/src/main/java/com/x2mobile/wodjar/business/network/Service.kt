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

    private val ENDPOINT_URL = "https://wodjar.herokuapp.com/api/v1/"

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

    fun getPersonalRecordTypes(callback: Callback<PersonalRecordTypesResponse> = PersonalRecordTypesCallback()) {
        val call = api.getPersonalRecordTypes()
        call.enqueue(callback)
    }

    fun getPersonalRecords(name: String, callback: Callback<PersonalRecordsResponse> = PersonalRecordsCallback()) {
        val call = api.getPersonalRecords(name)
        call.enqueue(callback)
    }

    fun savePersonalRecord(personalRecord: PersonalRecord, callback: Callback<PersonalRecord> = AddPersonalRecordCallback()) {
        val call = api.savePersonalRecord(personalRecord)
        call.enqueue(callback)
    }

    fun updatePersonalRecord(personalRecord: PersonalRecord, callback: Callback<Void> = UpdatePersonalRecordCallback()) {
        val call = api.updatePersonalRecord(personalRecord)
        call.enqueue(callback)
    }

    fun deletePersonalRecord(id: Int, callback: Callback<Void> = DeletePersonalRecordCallback()) {
        val call = api.deletePersonalRecord(id)
        call.enqueue(callback)
    }

    fun deletePersonalRecords(ids: List<Int>, callback: Callback<Void> = DeletePersonalRecordsCallback()) {
        val call = api.deletePersonalRecords(ids)
        call.enqueue(callback)
    }

    fun updatePersonalRecordType(ids: List<Int>, name: String, callback: Callback<Void> = UpdatePersonalRecordTypeCallback()) {
        val call = api.updatePersonalRecords(ids, name)
        call.enqueue(callback)
    }

    fun deletePersonalRecordType(name: String, callback: Callback<Void> = DeletePersonalRecordTypeCallback()) {
        val call = api.deletePersonalRecords(name)
        call.enqueue(callback)
    }

    fun getDefaultWorkouts(callback: Callback<WorkoutsResponse> = WorkoutsCallback()) {
        val call = api.getDefaultWorkouts()
        call.enqueue(callback)
    }

    fun getWorkouts(callback: Callback<WorkoutsResponse> = WorkoutsCallback()) {
        val call = api.getWorkouts()
        call.enqueue(callback)
    }

    fun saveWorkout(workout: Workout, callback: Callback<Workout> = AddWorkoutCallback()) {
        val call = api.saveWorkout(workout)
        call.enqueue(callback)
    }

    fun updateWorkout(workout: Workout, callback: Callback<Void> = UpdateWorkoutCallback()) {
        val call = api.updateWorkout(workout.id, workout)
        call.enqueue(callback)
    }

    fun updateWorkout(workoutId: Int, default: Boolean, favorite: Boolean, callback: Callback<Void> = UpdateWorkoutCallback()) {
        val call = api.updateWorkout(workoutId, default, favorite)
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