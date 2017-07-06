package com.x2mobile.wodjar.fragments

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.business.Constants
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.*
import com.x2mobile.wodjar.data.model.UnitType
import com.x2mobile.wodjar.data.model.Workout
import com.x2mobile.wodjar.data.model.WorkoutResult
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.support.v4.toast

class WorkoutResultFragment : ResultFragment<WorkoutResult>() {

    val workout: Workout by lazy { arguments!![NavigationConstants.KEY_WORKOUT] as Workout }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbarDelegate.title = workout.name!!
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val descriptionContainer = LayoutInflater.from(context).inflate(R.layout.workout_description, binding.headerContainer, true)
        (descriptionContainer.findViewById(R.id.description) as TextView).text = if (Preference.getUnitType(context) == UnitType.IMPERIAL ||
                TextUtils.isEmpty(workout.metricDescription)) workout.description else workout.metricDescription
    }

    override fun createResult(): WorkoutResult {
        val workoutResult = WorkoutResult()
        workoutResult.workoutId = workout.id
        workoutResult.default = workout.default
        workoutResult.type = workout.resultType
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

    override fun prepareShareText(result: WorkoutResult): String {
        return workout.description + "\n\n" + super.prepareShareText(result)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutResultAdded(requestResponseEvent: AddWorkoutResultRequestEvent) {
        progress?.dismiss()
        if (requestResponseEvent.response.body() != null) {
            val result = requestResponseEvent.response.body()!!
            result.type = workout.resultType

            activity.setResult(Activity.RESULT_OK, context.intentFor<Any>(NavigationConstants.KEY_RESULT to result))
            activity.finish()
        } else {
            toast(R.string.error_occurred)
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