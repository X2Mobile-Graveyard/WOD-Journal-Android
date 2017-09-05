package com.x2mobile.wodjar.fragments

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.WorkoutCustomActivity
import com.x2mobile.wodjar.activity.WorkoutEditActivity
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.data.event.WorkoutsCustomRequestEvent
import com.x2mobile.wodjar.data.model.WorkoutCustom
import com.x2mobile.wodjar.fragments.base.WorkoutBaseListFragment
import com.x2mobile.wodjar.ui.adapter.WorkoutsCustomAdapter
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.intentFor

class WorkoutCustomListFragment : WorkoutBaseListFragment<WorkoutCustom>() {

    override val adapter: WorkoutsCustomAdapter by lazy { WorkoutsCustomAdapter(context, this) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.workouts_custom, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val add = view.findViewById<FloatingActionButton>(R.id.add)
        add.setOnClickListener {
            if (Preference.isLoggedIn(context)) {
                startActivity(context.intentFor<WorkoutEditActivity>())
            } else {
                showLoginAlert()
            }
        }
    }

    override fun onWorkoutClicked(workout: WorkoutCustom) = startActivity(context.intentFor<WorkoutCustomActivity>(NavigationConstants.KEY_WORKOUT to workout))

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onWorkoutsResponse(requestResponseEvent: WorkoutsCustomRequestEvent) = handleWorkoutsResponse(requestResponseEvent)

    override fun sortWorkouts(workouts: List<WorkoutCustom>): List<WorkoutCustom> =
            workouts.sortedBy(WorkoutCustom::date).reversed()
}