package com.x2mobile.wodjar.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.business.Constants
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.*
import com.x2mobile.wodjar.data.model.Workout
import com.x2mobile.wodjar.data.model.WorkoutResult
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class WorkoutResultFragment : ResultFragment<WorkoutResult>() {

    var workout: Workout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        workout = arguments!![NavigationConstants.KEY_WORKOUT] as Workout

        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbarDelegate.title = workout!!.name!!
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val descriptionContainer = LayoutInflater.from(context).inflate(R.layout.workout_description, binding!!.headerContainer, true)
        (descriptionContainer.findViewById(R.id.description) as TextView).text = workout!!.description
    }

    override fun createResult(): WorkoutResult {
        val workoutResult = WorkoutResult()
        workoutResult.workoutId = workout!!.id
        workoutResult.default = workout!!.default
        workoutResult.type = workout!!.resultType
        return workoutResult
    }

    override fun saveResult(result: WorkoutResult) {
        if (result.id == Constants.ID_NA) {
            Service.saveWorkoutResult(result)
        } else {
            Service.updateWorkoutResult(result)
        }
    }

    override fun deleteResult(result: WorkoutResult) {
        Service.deleteWorkoutResult(result.id)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutResultAdded(requestResponseEvent: AddWorkoutResultRequestEvent) {
        progress?.dismiss()
        if (requestResponseEvent.response != null && requestResponseEvent.response.isSuccessful &&
                requestResponseEvent.response.body() != null) {
            activity.setResult(Activity.RESULT_OK, context.intentFor<Any>(NavigationConstants.KEY_RESULT to requestResponseEvent.response.body()))
            activity.finish()
        } else {
            context.toast(R.string.error_occurred)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutResultUpdated(requestResponseEvent: UpdateWorkoutResultRequestEvent) {
        progress?.dismiss()
        activity.setResult(Activity.RESULT_OK, context.intentFor<Any>(NavigationConstants.KEY_RESULT to result))
        activity.finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutResultDeleted(requestResponseEvent: DeleteWorkoutResultRequestEvent) {
        progress?.dismiss()
        activity.finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutResultAddFailed(requestFailureEvent: AddWorkoutResultRequestFailureEvent) {
        progress?.dismiss()
        handleRequestFailure(requestFailureEvent.throwable)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutResultUpdateFailed(requestFailureEvent: UpdateWorkoutResultRequestFailureEvent) {
        progress?.dismiss()
        handleRequestFailure(requestFailureEvent.throwable)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutResultDeleteFailed(requestFailureEvent: DeleteWorkoutResultRequestFailureEvent) {
        progress?.dismiss()
        handleRequestFailure(requestFailureEvent.throwable)
    }
}