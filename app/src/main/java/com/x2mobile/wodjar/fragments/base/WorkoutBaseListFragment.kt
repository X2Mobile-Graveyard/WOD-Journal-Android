package com.x2mobile.wodjar.fragments.base

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.data.event.base.RequestResponseEvent
import com.x2mobile.wodjar.data.model.base.BaseWorkout
import com.x2mobile.wodjar.ui.adapter.base.WorkoutsBaseAdapter
import com.x2mobile.wodjar.ui.callback.WorkoutListener
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.support.v4.toast

abstract class WorkoutBaseListFragment<T : BaseWorkout> : BaseFragment(), WorkoutListener<T> {

    protected abstract val adapter: WorkoutsBaseAdapter<T>

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

    override fun onResume() {
        super.onResume()

        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()

        EventBus.getDefault().unregister(this)
    }

    protected fun handleWorkoutsResponse(requestResponseEvent: RequestResponseEvent<MutableList<T>>) {
        if (requestResponseEvent.response.body() != null) {
            adapter.setItems(sortWorkouts(requestResponseEvent.response.body()!!).toMutableList())
        } else {
            toast(R.string.error_occurred)
        }
    }

    abstract fun sortWorkouts(workouts: List<T>): List<T>

}