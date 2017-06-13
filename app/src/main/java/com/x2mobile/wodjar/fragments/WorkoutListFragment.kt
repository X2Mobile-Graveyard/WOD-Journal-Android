package com.x2mobile.wodjar.fragments

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.WorkoutActivity
import com.x2mobile.wodjar.data.event.WorkoutsRequestEvent
import com.x2mobile.wodjar.data.model.Workout
import com.x2mobile.wodjar.data.model.WorkoutType
import com.x2mobile.wodjar.fragments.base.BaseFragment
import com.x2mobile.wodjar.ui.adapter.WorkoutsAdapter
import com.x2mobile.wodjar.ui.callback.WorkoutListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

open class WorkoutListFragment : BaseFragment(), WorkoutListener {

    val category: WorkoutType? by lazy { arguments!![KEY_WORKOUT_TYPE] as WorkoutType }

    val adapter: WorkoutsAdapter by lazy { WorkoutsAdapter(context, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)

        val searchView = menu.findItem(R.id.search_menu).actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter(newText)
                return true
            }
        })
    }

    override fun onStart() {
        super.onStart()

        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()

        EventBus.getDefault().unregister(this)
    }

    override fun onWorkoutClicked(workout: Workout) {
        startActivity(context.intentFor<WorkoutActivity>(WorkoutFragment.KEY_WORKOUT to workout))
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    open fun onWorkoutsResponse(requestResponseEvent: WorkoutsRequestEvent) {
        if (requestResponseEvent.response != null && requestResponseEvent.response.isSuccessful &&
                requestResponseEvent.response.body() != null) {
            val workouts = requestResponseEvent.response.body()!!.workouts.filter {
                it.type == category
            }
            adapter.setItems(workouts.toMutableList())
        } else {
            context.toast(R.string.error_occurred)
        }
    }

    companion object {
        val KEY_WORKOUT_TYPE = "type"
        val KEY_FAVORITES = "favorites"
    }

}
