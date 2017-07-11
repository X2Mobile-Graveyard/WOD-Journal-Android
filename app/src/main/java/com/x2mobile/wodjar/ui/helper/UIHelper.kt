package com.x2mobile.wodjar.ui.helper

import android.content.Context
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.data.model.ResultType
import com.x2mobile.wodjar.data.model.UnitType
import com.x2mobile.wodjar.util.MathUtil
import com.x2mobile.wodjar.util.TimeUtil

object UIHelper {

    fun formatResult(context: Context, value: Float, type: ResultType): String? {
        if (value > 0) {
            return when (type) {
                ResultType.WEIGHT -> context.getString(R.string.weight_prefix,
                        context.getString(if (Preference.getUnitType(context) == UnitType.METRIC) R.string.kg_suffix else R.string.lb_suffix,
                                MathUtil.convertWeight(value, UnitType.METRIC, Preference.getUnitType(context))))
                ResultType.REPETITION -> context.getString(R.string.reps_prefix, value.toInt())
                ResultType.TIME -> context.getString(R.string.time_prefix, TimeUtil.formatTime(value.toLong()))
            }
        }
        return null
    }

}
