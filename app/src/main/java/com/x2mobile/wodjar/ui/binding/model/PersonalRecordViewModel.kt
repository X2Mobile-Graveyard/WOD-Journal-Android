package com.x2mobile.wodjar.ui.binding.model

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.x2mobile.wodjar.BR
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.data.model.PersonalRecord
import com.x2mobile.wodjar.data.model.ResultType
import com.x2mobile.wodjar.data.model.UnitType

class PersonalRecordViewModel(val context: Context, val personalRecord: PersonalRecord) : BaseObservable() {

    @Bindable
    val weightEnabled: Boolean = personalRecord.type == ResultType.OTHER || personalRecord.type == ResultType.WEIGHT

    @Bindable
    var weightSelected: Boolean = personalRecord.type == ResultType.OTHER || personalRecord.type == ResultType.WEIGHT
        set(value) {
            field = value
            notifyPropertyChanged(BR.weightSelected)
        }

    @Bindable
    val repsEnabled: Boolean = personalRecord.type == ResultType.OTHER || personalRecord.type == ResultType.REPETITION

    @Bindable
    var repsSelected: Boolean = personalRecord.type == ResultType.REPETITION
        set(value) {
            field = value
            notifyPropertyChanged(BR.repsSelected)
        }

    @Bindable
    val timeEnabled: Boolean = personalRecord.type == ResultType.OTHER || personalRecord.type == ResultType.TIME

    @Bindable
    var timeSelected: Boolean = personalRecord.type == ResultType.TIME
        set(value) {
            field = value
            notifyPropertyChanged(BR.timeSelected)
        }

    @Bindable
    fun isImageAvailable(): Boolean {
        return personalRecord.imageUri != null
    }

    @Bindable
    fun getWeightLiftedHint(): String {
        return context.getString(R.string.weight_lifted, context.getString(if (Preference.getUnitType(context) ==
                UnitType.METRIC) R.string.kg else R.string.lb))
    }

    fun notifyImageChange() {
        notifyPropertyChanged(BR.imageAvailable)
    }

    fun notifyTimeResultChange() {
        personalRecord.notifyPropertyChanged(BR.resultTime)
    }

    fun notifyDateChange() {
        personalRecord.notifyPropertyChanged(BR.date)
    }
}