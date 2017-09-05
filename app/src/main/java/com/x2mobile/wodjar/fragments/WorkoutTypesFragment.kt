package com.x2mobile.wodjar.fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.LoggedInEvent
import com.x2mobile.wodjar.data.event.LoggedOutEvent
import com.x2mobile.wodjar.data.event.WorkoutsRequestFailureEvent
import com.x2mobile.wodjar.data.model.WorkoutType
import com.x2mobile.wodjar.fragments.base.BaseFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.support.v4.onPageChangeListener
import java.lang.UnsupportedOperationException

class WorkoutTypesFragment : BaseFragment() {

    private val KEY_SELECTED_TAB = "selected_tab"

    private lateinit var selectedTab: WorkoutTab

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        EventBus.getDefault().register(this)

        selectedTab = savedInstanceState?.getSerializable(KEY_SELECTED_TAB) as? WorkoutTab ?: WorkoutTab.CUSTOM

        fetchWorkouts(selectedTab.workoutType)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbarDelegate.title = getString(R.string.workouts)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.workout_types, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = view.findViewById<ViewPager>(R.id.view_pager)
        viewPager.adapter = WorkoutTypesPagerAdapter(context, childFragmentManager)
        viewPager.onPageChangeListener {
            onPageSelected { position ->
                selectedTab = WorkoutTab.values()[position]
                fetchWorkouts(selectedTab.workoutType)
            }
        }

        val tabLayout = view.findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_SELECTED_TAB, selectedTab)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoggedIn(event: LoggedInEvent) = fetchWorkouts(selectedTab.workoutType)

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoggedOut(event: LoggedOutEvent) = fetchWorkouts(selectedTab.workoutType)

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutsFailure(requestFailureEvent: WorkoutsRequestFailureEvent) = handleRequestFailure(requestFailureEvent.throwable)

    private fun fetchWorkouts(type: WorkoutType) {
        if (Preference.isLoggedIn(context)) {
            if (type == WorkoutType.CUSTOM) {
                Service.getWorkoutsCustom()
            } else {
                Service.getWorkouts(type)
            }
        } else {
            if (type != WorkoutType.CUSTOM) {
                Service.getDefaultWorkouts(type)
            }
        }
    }

    class WorkoutTypesPagerAdapter(val context: Context, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment = when (position) {
            WorkoutTab.CUSTOM.ordinal -> WorkoutCustomListFragment()
            WorkoutTab.GIRLS.ordinal -> WorkoutGirlsListFragment()
            WorkoutTab.HEROES.ordinal -> WorkoutHeroesListFragment()
            WorkoutTab.CHALLENGES.ordinal -> WorkoutChallengesListFragment()
            WorkoutTab.OPENS.ordinal -> WorkoutOpensListFragment()
            else -> throw UnsupportedOperationException()
        }

        override fun getPageTitle(position: Int): CharSequence = WorkoutTab.values()[position].toString()

        override fun getCount(): Int = WorkoutTab.values().size

    }

    enum class WorkoutTab(val workoutType: WorkoutType) {
        CUSTOM(WorkoutType.CUSTOM), GIRLS(WorkoutType.GIRLS), HEROES(WorkoutType.HEROES),
        CHALLENGES(WorkoutType.CHALLENGES), OPENS(WorkoutType.OPENS)
    }
}
