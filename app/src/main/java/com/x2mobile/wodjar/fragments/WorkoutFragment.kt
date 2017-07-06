package com.x2mobile.wodjar.fragments

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
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
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.*
import com.x2mobile.wodjar.data.event.base.RequestResponseEvent
import com.x2mobile.wodjar.data.model.Workout
import com.x2mobile.wodjar.data.model.WorkoutResult
import com.x2mobile.wodjar.data.model.WorkoutType
import com.x2mobile.wodjar.data.model.best
import com.x2mobile.wodjar.databinding.WorkoutBinding
import com.x2mobile.wodjar.fragments.base.BaseFragment
import com.x2mobile.wodjar.ui.adapter.WorkoutResultsAdapter
import com.x2mobile.wodjar.ui.binding.model.WorkoutViewModel
import com.x2mobile.wodjar.ui.callback.WorkoutResultListener
import com.x2mobile.wodjar.ui.helper.ImageViewer
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.support.v4.toast
import kotlin.reflect.KClass

open class WorkoutFragment : BaseFragment(), WorkoutResultListener {

    val REQUEST_CODE_WORKOUT_RESULT = 19

    val REQUEST_CODE_EDIT_WORKOUT = 33

    val TAG_YOUTUBE_PLAYER = "video_player"

    lateinit var workout: Workout

    lateinit var binding: WorkoutBinding

    val adapter: WorkoutResultsAdapter by lazy { WorkoutResultsAdapter(context, this) }

    val imageViewer: ImageViewer by lazy { ImageViewer(this, binding.image) }

    val requestEventType: KClass<out RequestResponseEvent<MutableList<Workout>>> by lazy {
        when (workout.type) {
            WorkoutType.CUSTOM -> WorkoutCustomsRequestEvent::class
            WorkoutType.GIRLS -> WorkoutGirlsRequestEvent::class
            WorkoutType.HEROES -> WorkoutHeroesRequestEvent::class
            WorkoutType.CHALLENGES -> WorkoutChallengesRequestEvent::class
            WorkoutType.OPENS -> WorkoutOpensRequestEvent::class
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        EventBus.getDefault().register(this)

        workout = arguments!![NavigationConstants.KEY_WORKOUT] as Workout

        if (Preference.isLoggedIn(context)) {
            Service.getWorkout(workout.id, workout.type)
            Service.getWorkoutResults(workout.id)
        } else {
            Service.getDefaultWorkout(workout.id, workout.type)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbarDelegate.title = workout.name!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<WorkoutBinding>(inflater, R.layout.workout, container, false)
        binding.viewModel = WorkoutViewModel(context, workout)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewer.imageUri = workout.imageUri

        initYoutubePlayer(workout.video)

        binding.image.setOnClickListener {
            imageViewer.popup(binding.imageContainer)
        }

        binding.history.setOnClickListener {
            val fragment = HistoryFragment()
            fragment.arguments = bundleOf(HistoryFragment.KEY_HISTORY to workout.history!!)
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment, null).addToBackStack(null).commit()
        }

        binding.add.setOnClickListener {
            if (Preference.isLoggedIn(context)) {
                startActivityForResult(context.intentFor<WorkoutResultActivity>(NavigationConstants.KEY_WORKOUT to workout), REQUEST_CODE_WORKOUT_RESULT)
            } else {
                showLoginAlert()
            }
        }

        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_WORKOUT_RESULT) {
            val workoutResult = data?.getParcelableExtra<WorkoutResult>(NavigationConstants.KEY_RESULT)
            when (resultCode) {
                Activity.RESULT_OK -> {
                    var position = adapter.getItemPosition(workoutResult!!)
                    if (position >= 0) {
                        adapter.removeItem(position)
                    } else {
                        position = 0
                    }
                    adapter.addItem(workoutResult, position)
                }
                NavigationConstants.RESULT_DELETED -> adapter.removeItem(workoutResult!!)
            }

            //Updating the cached version
            val workouts = EventBus.getDefault().getStickyEvent(requestEventType.java).response.body()!!
            val workout = workouts.find { it.id == workoutResult?.workoutId }
            workout?.bestResult = adapter.getItems()?.best { it.result }?.result ?: 0f
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onWorkoutResultClicked(workoutResult: WorkoutResult) {
        startActivityForResult(context.intentFor<WorkoutResultActivity>(NavigationConstants.KEY_RESULT to workoutResult,
                NavigationConstants.KEY_WORKOUT to workout), REQUEST_CODE_WORKOUT_RESULT)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutResultsResponse(requestResponseEvent: WorkoutResultsRequestEvent) {
        if (requestResponseEvent.response.body() != null) {
            val workoutResults = requestResponseEvent.response.body()!!.workoutResults
                    .sortedBy(WorkoutResult::date).toMutableList()
            workoutResults.forEach { it.type = workout.resultType }
            adapter.setItems(workoutResults)
        } else {
            toast(R.string.error_occurred)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutResponse(requestResponseEvent: WorkoutRequestEvent) {
        if (requestResponseEvent.response.body() != null) {
            workout = requestResponseEvent.response.body()!!

            binding.viewModel = WorkoutViewModel(context, workout)
            imageViewer.imageUri = workout.imageUri
            initYoutubePlayer(workout.video)

            arguments.putParcelable(NavigationConstants.KEY_WORKOUT, workout)
        } else {
            toast(R.string.error_occurred)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutResultsFailure(requestFailureEvent: WorkoutResultsRequestFailureEvent) {
        handleRequestFailure(requestFailureEvent.throwable)
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutFailure(requestFailureEvent: WorkoutRequestFailureEvent) {
        handleRequestFailure(requestFailureEvent.throwable)
    }

    fun initYoutubePlayer(video: String?) {
        if (!TextUtils.isEmpty(video)) {
            var player = fragmentManager.findFragmentByTag(TAG_YOUTUBE_PLAYER) as? YouTubePlayerSupportFragment
            if (player == null) {
                player = YouTubePlayerSupportFragment.newInstance()
                fragmentManager.beginTransaction().replace(R.id.player_container, player, TAG_YOUTUBE_PLAYER).commit()
            }
            player!!.initialize(Constants.YOUTUBE_API_KEY, YoutubeInitializedListener(workout))
        }
    }

    class YoutubeInitializedListener(val workout: Workout) : YouTubePlayer.OnInitializedListener {

        override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
            player?.cueVideo(workout.video)
        }

        override fun onInitializationFailure(provider: YouTubePlayer.Provider?, error: YouTubeInitializationResult?) {
        }

    }
}
