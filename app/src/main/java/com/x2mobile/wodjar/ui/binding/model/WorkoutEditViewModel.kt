package com.x2mobile.wodjar.ui.binding.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.x2mobile.wodjar.BR
import com.x2mobile.wodjar.data.model.ResultType
import com.x2mobile.wodjar.data.model.Workout

class WorkoutEditViewModel(val workout: Workout) : BaseObservable() {

    @Bindable
    var description: String? = workout.description
        set(value) {
            field = value
            workout.description = value
        }

    @Bindable
    var weightSelected: Boolean = workout.resultType == ResultType.WEIGHT || workout.resultType == ResultType.OTHER
        set(value) {
            field = value
            workout.resultType = ResultType.WEIGHT
        }

    @Bindable
    var repsSelected: Boolean = workout.resultType == ResultType.REPETITION
        set(value) {
            field = value
            workout.resultType = ResultType.REPETITION
        }

    @Bindable
    var timeSelected: Boolean = workout.resultType == ResultType.TIME
        set(value) {
            field = value
            workout.resultType = ResultType.TIME
        }

    @Bindable
    fun isImageAvailable(): Boolean {
        return workout.imageUri != null
    }

    fun notifyImageChange() {
        notifyPropertyChanged(BR.imageAvailable)
    }

}