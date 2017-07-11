package com.x2mobile.wodjar.ui.binding.model

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.x2mobile.wodjar.BR
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.data.model.Result
import com.x2mobile.wodjar.data.model.ResultType
import com.x2mobile.wodjar.data.model.UnitType
import com.x2mobile.wodjar.util.MathUtil

class ResultViewModel(val context: Context, val result: Result) : BaseObservable() {

    @Bindable
    var weightSelected: Boolean = result.type == ResultType.WEIGHT
        set(value) {
            field = value
            if (value) {
                result.type = ResultType.WEIGHT
            }
            notifyPropertyChanged(BR.weightSelected)
        }

    @Bindable
    var repsSelected: Boolean = result.type == ResultType.REPETITION
        set(value) {
            field = value
            if (value) {
                result.type = ResultType.REPETITION
            }
            notifyPropertyChanged(BR.repsSelected)
        }

    @Bindable
    var timeSelected: Boolean = result.type == ResultType.TIME
        set(value) {
            field = value
            if (value) {
                result.type = ResultType.TIME
            }
            notifyPropertyChanged(BR.timeSelected)
        }

    var resultWeight: Float = MathUtil.convertWeight(result.resultWeight, UnitType.METRIC, Preference.getUnitType(context))
        set(value) {
            field = value
            result.resultWeight = MathUtil.convertWeight(value, Preference.getUnitType(context), UnitType.METRIC)
        }

    @Bindable
    fun getWeightLiftedHint(): String {
        return context.getString(R.string.weight_lifted, context.getString(if (Preference.getUnitType(context) ==
                UnitType.METRIC) R.string.kg else R.string.lb))
    }

    @Bindable
    fun isImageAvailable(): Boolean {
        return result.imageUri != null
    }

    fun notifyImageChange() {
        notifyPropertyChanged(BR.imageAvailable)
    }

    fun notifyTimeResultChange() {
        result.notifyPropertyChanged(BR.resultTime)
    }

    fun notifyDateChange() {
        result.notifyPropertyChanged(BR.date)
    }
}