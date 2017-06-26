package com.x2mobile.wodjar.ui.adapter

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.data.model.ResultType
import com.x2mobile.wodjar.data.model.WorkoutResult
import com.x2mobile.wodjar.ui.adapter.base.BaseAdapter
import com.x2mobile.wodjar.ui.adapter.base.BaseViewHolder
import com.x2mobile.wodjar.ui.callback.WorkoutResultListener
import com.x2mobile.wodjar.util.TimeUtil

class WorkoutResultsAdapter(val context: Context, val callback: WorkoutResultListener) : BaseAdapter<WorkoutResult, WorkoutResultsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): WorkoutResultsViewHolder {
        return WorkoutResultsViewHolder(LayoutInflater.from(context).inflate(R.layout.workout_result_item, parent, false), callback)
    }

    public override fun getItems(): MutableList<WorkoutResult>? {
        return super.getItems()
    }
}

class WorkoutResultsViewHolder(itemView: View, val callback: WorkoutResultListener) : BaseViewHolder<WorkoutResult>(itemView) {

    val date: TextView by lazy { itemView.findViewById(R.id.date) as TextView }
    val result: TextView by lazy { itemView.findViewById(R.id.result) as TextView }

    override fun bindData(item: WorkoutResult) {
        itemView.setOnClickListener { callback.onWorkoutResultClicked(item) }
        date.text = DateFormat.getMediumDateFormat(itemView.context).format(item.date)
        result.text = when (item.type) {
            ResultType.TIME -> TimeUtil.formatTime(item.result.toLong())
            else -> item.result.toString()
        }
    }

}
