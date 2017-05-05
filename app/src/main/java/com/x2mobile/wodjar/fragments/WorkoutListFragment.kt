package com.x2mobile.wodjar.fragments

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.data.event.WorkoutsRequestEvent
import com.x2mobile.wodjar.data.model.Workout
import com.x2mobile.wodjar.data.model.WorkoutCategory
import com.x2mobile.wodjar.fragments.base.BaseFragment
import com.x2mobile.wodjar.ui.adapter.WorkoutsAdapter
import com.x2mobile.wodjar.ui.callback.WorkoutListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast

class WorkoutListFragment : BaseFragment(), WorkoutListener {

    var category: WorkoutCategory? = null
    var favorites: Boolean = false

    val adapter: WorkoutsAdapter by lazy { WorkoutsAdapter(context, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        category = arguments!![KEY_WORKOUT_CATEGORY] as? WorkoutCategory
        favorites = arguments!!.getBoolean(KEY_FAVORITES, false)

        EventBus.getDefault().register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.workouts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }

    override fun onWorkoutClicked(workout: Workout) {

    }

    override fun onWorkoutFavoriteToggle(workout: Workout) {
        workout.favorite = !workout.favorite
        adapter.notifyItemChanged(adapter.getItemPosition(workout))
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onWorkoutsResponse(requestResponseEvent: WorkoutsRequestEvent) {
        if (requestResponseEvent.response != null && requestResponseEvent.response.isSuccessful &&
                requestResponseEvent.response.body() != null) {
            val workouts = requestResponseEvent.response.body().workouts!!.filter {
                if (category == null) it.favorite == favorites else it.category == category
            }
            adapter.setItems(workouts.toMutableList())
        } else {
            context.toast(R.string.error_occurred)
        }
    }

    companion object {
        val KEY_WORKOUT_CATEGORY = "category"
        val KEY_FAVORITES = "favorites"
    }

}
