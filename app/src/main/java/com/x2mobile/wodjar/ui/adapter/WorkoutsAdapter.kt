package com.x2mobile.wodjar.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.data.model.UnitType
import com.x2mobile.wodjar.data.model.Workout
import com.x2mobile.wodjar.ui.adapter.base.BaseViewHolder
import com.x2mobile.wodjar.ui.adapter.base.FilterableBaseAdapter
import com.x2mobile.wodjar.ui.callback.WorkoutListener
import com.x2mobile.wodjar.ui.helper.UIHelper

class WorkoutsAdapter(val context: Context, val callback: WorkoutListener) : FilterableBaseAdapter<Workout, WorkoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder(LayoutInflater.from(context).inflate(R.layout.workout_item, parent, false), callback)
    }

}

class WorkoutViewHolder(itemView: View, val callback: WorkoutListener) : BaseViewHolder<Workout>(itemView) {

    val name: TextView by lazy { itemView.findViewById(R.id.name) as TextView }
    val description: TextView by lazy { itemView.findViewById(R.id.description) as TextView }
    val result: TextView by lazy { itemView.findViewById(R.id.best_result) as TextView }

    override fun bindData(item: Workout) {
        val context = itemView.context
        itemView.setOnClickListener { callback.onWorkoutClicked(item) }
        name.text = item.name
        description.text = if (Preference.getUnitType(context) == UnitType.IMPERIAL ||
                TextUtils.isEmpty(item.metricDescription)) item.description else item.metricDescription
        result.text = UIHelper.formatResult(context, item.bestResult, item.resultType)
    }

}
