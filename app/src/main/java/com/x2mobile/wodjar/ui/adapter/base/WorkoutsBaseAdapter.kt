package com.x2mobile.wodjar.ui.adapter.base

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.data.model.UnitType
import com.x2mobile.wodjar.data.model.base.BaseWorkout
import com.x2mobile.wodjar.ui.callback.WorkoutListener
import com.x2mobile.wodjar.ui.helper.UIHelper

abstract class WorkoutsBaseAdapter<T : BaseWorkout> : FilterableBaseAdapter<T, WorkoutsBaseViewHolder<T>>()

open class WorkoutsBaseViewHolder<in T : BaseWorkout>(itemView: View, val callback: WorkoutListener<T>) : BaseViewHolder<T>(itemView) {

    val context: Context by lazy { itemView.context }
    val description: TextView by lazy { itemView.findViewById(R.id.description) as TextView }
    val result: TextView by lazy { itemView.findViewById(R.id.best_result) as TextView }

    override fun bindData(item: T) {
        itemView.setOnClickListener { callback.onWorkoutClicked(item) }
        description.text = if (Preference.getUnitType(context) == UnitType.IMPERIAL ||
                TextUtils.isEmpty(item.metricDescription)) item.description else item.metricDescription
        result.text = UIHelper.formatResult(context, item.bestResult, item.resultType)
    }

}