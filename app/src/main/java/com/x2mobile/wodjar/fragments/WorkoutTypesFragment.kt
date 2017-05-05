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
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.WorkoutsRequestFailureEvent
import com.x2mobile.wodjar.data.model.WorkoutCategory
import com.x2mobile.wodjar.fragments.base.BaseFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.bundleOf

class WorkoutTypesFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        EventBus.getDefault().register(this)

        Service.getWorkouts()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbarDelegate.title = getString(R.string.workouts)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.workout_types, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = view.findViewById(R.id.view_pager) as ViewPager
        viewPager.adapter = WorkoutTypesPagerAdapter(context, childFragmentManager)

        val tabLayout = view.findViewById(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutsFailure(requestFailureEvent: WorkoutsRequestFailureEvent) {
        handleRequestFailure(requestFailureEvent.throwable)
    }

    class WorkoutTypesPagerAdapter(val context: Context, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment {
            val fragment: Fragment = WorkoutListFragment()
            if (position < WorkoutCategory.values().size) {
                fragment.arguments = bundleOf(WorkoutListFragment.KEY_WORKOUT_CATEGORY to WorkoutCategory.values()[position])
            } else {
                fragment.arguments = bundleOf(WorkoutListFragment.KEY_FAVORITES to true)
            }
            return fragment
        }

        override fun getPageTitle(position: Int): CharSequence {
            return if (position < WorkoutCategory.values().size) WorkoutCategory.values()[position].toString() else
                context.getString(R.string.favorites)
        }

        override fun getCount(): Int {
            return WorkoutCategory.values().size + 1 // the extra one is favorites tab
        }

    }
}
