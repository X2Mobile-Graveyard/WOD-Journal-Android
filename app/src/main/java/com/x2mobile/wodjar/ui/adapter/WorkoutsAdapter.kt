package com.x2mobile.wodjar.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.data.model.Workout
import com.x2mobile.wodjar.ui.adapter.base.BaseAdapter
import com.x2mobile.wodjar.ui.adapter.base.BaseViewHolder
import com.x2mobile.wodjar.ui.callback.WorkoutListener
import org.jetbrains.anko.onClick

class WorkoutsAdapter(val context: Context, val callback: WorkoutListener) : BaseAdapter<Workout, WorkoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder(LayoutInflater.from(context).inflate(R.layout.workout_item, parent, false), callback)
    }

}

class WorkoutViewHolder(itemView: View, val callback: WorkoutListener) : BaseViewHolder<Workout>(itemView) {

    val name: TextView by lazy { itemView.findViewById(R.id.name) as TextView }
    val favorite: ImageView by lazy { itemView.findViewById(R.id.favorite) as ImageView }

    override fun bindData(item: Workout) {
        itemView.onClick { callback.onWorkoutClicked(item) }
        name.text = item.name
        favorite.setImageResource(if (item.favorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border)
        favorite.onClick { callback.onWorkoutFavoriteToggle(item) }
    }

}
