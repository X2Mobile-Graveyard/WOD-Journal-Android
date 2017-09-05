package com.x2mobile.wodjar.ui.adapter

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.data.model.WorkoutCustom
import com.x2mobile.wodjar.ui.adapter.base.WorkoutsBaseAdapter
import com.x2mobile.wodjar.ui.adapter.base.WorkoutsBaseViewHolder
import com.x2mobile.wodjar.ui.callback.WorkoutListener

class WorkoutsCustomAdapter(val context: Context, val callback: WorkoutListener<WorkoutCustom>) : WorkoutsBaseAdapter<WorkoutCustom>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): WorkoutsCustomViewHolder = WorkoutsCustomViewHolder(LayoutInflater.from(context).inflate(R.layout.workout_custom_item, parent, false), callback)

}

class WorkoutsCustomViewHolder(itemView: View, callback: WorkoutListener<WorkoutCustom>) : WorkoutsBaseViewHolder<WorkoutCustom>(itemView, callback) {

    val date: TextView by lazy { itemView.findViewById<TextView>(R.id.date) }

    override fun bindData(item: WorkoutCustom) {
        super.bindData(item)
        date.text = DateFormat.getMediumDateFormat(context).format(item.date)
    }

}
