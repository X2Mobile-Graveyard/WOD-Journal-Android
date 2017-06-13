package com.x2mobile.wodjar.fragments

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.*
import com.bumptech.glide.Glide
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.ImageViewer
import com.x2mobile.wodjar.activity.WorkoutResultActivity
import com.x2mobile.wodjar.business.Constants
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.WorkoutResultsRequestEvent
import com.x2mobile.wodjar.data.event.WorkoutResultsRequestFailureEvent
import com.x2mobile.wodjar.data.event.WorkoutsRequestEvent
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

    val REQUEST_CODE_WORKOUT_RESULT = 19

    val TAG_YOUTUBE_PLAYER = "video_player"

    val workout: Workout by lazy {arguments!![KEY_WORKOUT] as Workout}

    lateinit var binding: WorkoutBinding

    val adapter: WorkoutResultsAdapter by lazy { WorkoutResultsAdapter(context, this) }

    val windowRect: Rect by lazy {
        val rect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)
        rect
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        EventBus.getDefault().register(this)

        if (Preference.isLoggedIn(context)) {
            Service.getWorkoutResults(workout.id)
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

        binding.image.onClick {
            val intent = context.intentFor<ImageViewer>()
            intent.putExtra(ImageViewer.KEY_URI, workout.imageUri)
            intent.putExtra(ImageViewer.KEY_RECT, Rect(binding.image.left, binding.image.top, binding.image.right,
                    binding.image.bottom))
            startActivity(intent)
        }

        Glide.with(context).load(workout.imageUri).override(windowRect.width(), windowRect.height()).into(binding.image)

        val history = view.findViewById(R.id.history)
        history.onClick {
            val fragment = HistoryFragment()
            fragment.arguments = bundleOf(HistoryFragment.KEY_HISTORY to workout.history!!)
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, null).addToBackStack(null).commit()
        }

        val add = view.findViewById(R.id.add)
        add.onClick {
            startActivity(context.intentFor<WorkoutResultActivity>(NavigationConstants.KEY_WORKOUT to workout))
        }

        if (!TextUtils.isEmpty(workout.video)) {
            val player: YouTubePlayerSupportFragment
            if (savedInstanceState == null) {
                player = YouTubePlayerSupportFragment.newInstance()
                fragmentManager.beginTransaction().replace(R.id.player_container, player, TAG_YOUTUBE_PLAYER).commit()
            } else {
                player = fragmentManager.findFragmentByTag(TAG_YOUTUBE_PLAYER) as YouTubePlayerSupportFragment
            }
            player.initialize(Constants.YOUTUBE_API_KEY, YoutubeInitializedListener(workout))
        }

        val recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_favourite, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.mark_favorite_menu).isVisible = !workout.favorite
        menu.findItem(R.id.remove_favorite_menu).isVisible = workout.favorite
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mark_favorite_menu || item.itemId == R.id.remove_favorite_menu) {
            workout.favorite = !workout.favorite
            arguments.putParcelable(KEY_WORKOUT, workout)

            activity.supportInvalidateOptionsMenu()

            //Updating the cached version
            val workouts = EventBus.getDefault().getStickyEvent(WorkoutsRequestEvent::class.java).response!!.body()!!.workouts
            val workout = workouts.find { it.id == workout.id }
            workout!!.favorite = this@WorkoutFragment.workout.favorite

            Service.updateWorkout(workout.id, workout.default, workout.favorite)
            return true
        }
        return super.onOptionsItemSelected(item)

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
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onWorkoutResultClicked(workoutResult: WorkoutResult) {
        startActivityForResult(context.intentFor<WorkoutResultActivity>(NavigationConstants.KEY_RESULT to workoutResult,
                NavigationConstants.KEY_WORKOUT to workout), REQUEST_CODE_WORKOUT_RESULT)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWorkoutResultsResponse(requestResponseEvent: WorkoutResultsRequestEvent) {
        if (requestResponseEvent.response != null && requestResponseEvent.response.isSuccessful &&
                requestResponseEvent.response.body() != null) {
            val workoutResults = requestResponseEvent.response.body()!!.workoutResults.toMutableList()
            workoutResults.forEach { it.type = workout.resultType }
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