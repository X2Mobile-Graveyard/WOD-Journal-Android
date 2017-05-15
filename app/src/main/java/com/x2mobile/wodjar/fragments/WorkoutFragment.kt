package com.x2mobile.wodjar.fragments

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.WorkoutResultActivity
import com.x2mobile.wodjar.business.Constants
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.WorkoutResultsRequestEvent
import com.x2mobile.wodjar.data.event.WorkoutResultsRequestFailureEvent
import com.x2mobile.wodjar.data.model.Workout
import com.x2mobile.wodjar.data.model.WorkoutResult
import com.x2mobile.wodjar.databinding.WorkoutBinding
import com.x2mobile.wodjar.fragments.base.BaseFragment
import com.x2mobile.wodjar.ui.adapter.WorkoutResultsAdapter
import com.x2mobile.wodjar.ui.binding.model.WorkoutViewModel
import com.x2mobile.wodjar.ui.callback.WorkoutResultListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast

class WorkoutFragment : BaseFragment(), WorkoutResultListener {

    val TAG_YOUTUBE_PLAYER = "video_player"

    var workout: Workout? = null

    val adapter: WorkoutResultsAdapter by lazy { WorkoutResultsAdapter(context, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        workout = arguments!![KEY_WORKOUT] as Workout

        EventBus.getDefault().register(this)

        if (Preference.isLoggedIn(context)) {
            Service.getWorkoutResults(workout!!.id)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbarDelegate.title = workout!!.name!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<WorkoutBinding>(inflater, R.layout.workout, container, false)
        binding.viewModel = WorkoutViewModel(workout!!)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!TextUtils.isEmpty(workout!!.video)) {
            val player: YouTubePlayerSupportFragment
            if (savedInstanceState == null) {
                player = YouTubePlayerSupportFragment.newInstance()
                fragmentManager.beginTransaction().replace(R.id.player_container, player, TAG_YOUTUBE_PLAYER).commit()
            } else {
                player = fragmentManager.findFragmentByTag(TAG_YOUTUBE_PLAYER) as YouTubePlayerSupportFragment
            }
            player.initialize(Constants.YOUTUBE_API_KEY, YoutubeInitializedListener(workout!!))
        }

        val recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        val history = view.findViewById(R.id.history)
        history.onClick {
            val fragment = HistoryFragment()
            fragment.arguments = bundleOf(HistoryFragment.KEY_HISTORY to workout!!.history!!)
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, null).addToBackStack(null).commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }

    override fun onWorkoutResultClicked(workoutResult: WorkoutResult) {
        startActivity(context.intentFor<WorkoutResultActivity>(WorkoutResultFragment.KEY_WORKOUT_RESULT to workoutResult))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutResultsResponse(requestResponseEvent: WorkoutResultsRequestEvent) {
        if (requestResponseEvent.response != null && requestResponseEvent.response.isSuccessful &&
                requestResponseEvent.response.body() != null) {
            val workoutResults = requestResponseEvent.response.body().workoutResults!!.toMutableList()
            workoutResults.forEach { it.type = workout!!.resultType }
            adapter.setItems(workoutResults)
        } else {
            context.toast(R.string.error_occurred)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutResultsFailure(requestFailureEvent: WorkoutResultsRequestFailureEvent) {
        handleRequestFailure(requestFailureEvent.throwable)
    }

    companion object {
        val KEY_WORKOUT = "workout"
    }

    class YoutubeInitializedListener(val workout: Workout) : YouTubePlayer.OnInitializedListener {

        override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
            player?.cueVideo(workout.video)
        }

        override fun onInitializationFailure(provider: YouTubePlayer.Provider?, error: YouTubeInitializationResult?) {
            System.out.println("ERRRORRR: " + error?.toString())
        }

    }
}