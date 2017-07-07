package com.x2mobile.wodjar.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.data.model.Workout
import com.x2mobile.wodjar.ui.adapter.base.WorkoutsBaseAdapter
import com.x2mobile.wodjar.ui.adapter.base.WorkoutsBaseViewHolder
import com.x2mobile.wodjar.ui.callback.WorkoutListener

class WorkoutsAdapter(val context: Context, val callback: WorkoutListener<Workout>) : WorkoutsBaseAdapter<Workout>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): WorkoutsViewHolder {
        return WorkoutsViewHolder(LayoutInflater.from(context).inflate(R.layout.workout_item, parent, false), callback)
    }

}

class WorkoutsViewHolder(itemView: View, callback: WorkoutListener<Workout>) : WorkoutsBaseViewHolder<Workout>(itemView, callback) {

    val name: TextView by lazy { itemView.findViewById(R.id.name) as TextView }

    override fun bindData(item: Workout) {
        super.bindData(item)
        name.text = item.name
    }

}
