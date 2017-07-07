package com.x2mobile.wodjar.ui.binding.model

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.text.TextUtils
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.data.model.UnitType
import com.x2mobile.wodjar.data.model.base.BaseWorkout

class WorkoutViewModel(context: Context, val workout: BaseWorkout) : BaseObservable() {

    @Bindable
    val description: String? = if (Preference.getUnitType(context) == UnitType.IMPERIAL ||
            TextUtils.isEmpty(workout.metricDescription)) workout.description else workout.metricDescription

}
