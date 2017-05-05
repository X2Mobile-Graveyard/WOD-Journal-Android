package com.x2mobile.wodjar.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.BuildConfig
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.LoginActivity
import com.x2mobile.wodjar.business.callback.NavigationDrawerCallback
import com.x2mobile.wodjar.data.event.LoggedInEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.intentFor

/**
 * Fragment used for managing interactions for and presentation of a navigation_header drawer.
 * See the [
   * design guidelines](https://developer.android.com/design/patterns/navigation-drawer.html#Interaction) for a complete explanation of the behaviors implemented here.
 */
class NavigationDrawerFragment : Fragment() {

    private val CONTACT_X2MOBILE_NET = "contact@x2mobile.net"
    private val MAILTO = "mailto"

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private var mCallbacks: NavigationDrawerCallback? = null

    private var selectedNavigationType: NavigationType? = null

    private var navigationView: NavigationView? = null

    private var avatar: ImageView? = null
    private var name: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        EventBus.getDefault().register(this)

        if (savedInstanceState != null) {
            selectedNavigationType = NavigationType.fromId(savedInstanceState.getInt(STATE_SELECTED_ID))
        } else {
            selectedNavigationType = NavigationType.PR
            mCallbacks!!.onNavigationItemSelected(selectedNavigationType!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.navigation, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationView = view as NavigationView?

        navigationView!!.setNavigationItemSelectedListener { item ->
            selectedNavigationType = NavigationType.fromId(item.itemId)
            if (selectedNavigationType == NavigationType.FEEDBACK) {
                var displayName = Preference.getDisplayName(context)
                if (TextUtils.isEmpty(displayName)) {
                    displayName = ""
                }

                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.fromParts(MAILTO, CONTACT_X2MOBILE_NET, null)
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_subject, getString(R.string.app_name)))
                emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.feedback_body, displayName, BuildConfig.VERSION_NAME,
                        Build.MODEL, Build.VERSION.SDK_INT))
                startActivity(Intent.createChooser(emailIntent, null))
                false
            } else if (selectedNavigationType == NavigationType.LOGIN) {
                startActivity(context.intentFor<LoginActivity>())
                false
            } else if (selectedNavigationType == NavigationType.LOGOUT) {
                EventBus.getDefault().removeAllStickyEvents()
                Preference.clear(context)
                handleUserInfo()
                false
            } else {
                mCallbacks!!.onNavigationItemSelected(selectedNavigationType!!)
                true
            }
        }
        navigationView!!.setCheckedItem(selectedNavigationType!!.id)
        val headerView = navigationView!!.getHeaderView(0)
        name = headerView.findViewById(R.id.name) as TextView
        avatar = headerView.findViewById(R.id.avatar) as ImageView
        handleUserInfo()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mCallbacks = context as NavigationDrawerCallback?
        } catch (e: ClassCastException) {
            throw ClassCastException("Activity must implement NavigationDrawerCallback.")
        }

    }

    override fun onDetach() {
        super.onDetach()
        mCallbacks = null
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putInt(STATE_SELECTED_ID, selectedNavigationType!!.id)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoggedIn(event: LoggedInEvent) {
        handleUserInfo()
    }

    fun handleUserInfo() {
        val isLoggedIn = Preference.isLoggedIn(context)
        navigationView!!.menu.findItem(R.id.logout).isVisible = isLoggedIn
        navigationView!!.menu.findItem(R.id.login).isVisible = !isLoggedIn
        val displayName = Preference.getDisplayName(context)
        if (!TextUtils.isEmpty(displayName)) {
            name!!.text = displayName
        } else {
            name!!.text = getString(R.string.no_user)
        }
        setProfilePicture()
    }

    fun setProfilePicture() {
        val width = resources.getDimensionPixelOffset(R.dimen.profile_image_size)
        Glide.with(context)
                .load(Preference.getProfilePictureUrl(context))
                .override(width, width)
                .centerCrop()
                .placeholder(R.drawable.default_avatar)
                .dontAnimate()
                .into(avatar!!)
    }

    enum class NavigationType constructor(@IdRes val id: Int) {

        PR(R.id.pr), WOD(R.id.wod), OPTIONS(R.id.options), FEEDBACK(R.id.feedback), LOGIN(R.id.login), LOGOUT(R.id.logout);

        companion object {

            fun fromId(@IdRes id: Int): NavigationType? {
                return values().firstOrNull { it.id == id }
            }
        }
    }

    companion object {

        /**
         * Remember the position of the selected item.
         */
        private val STATE_SELECTED_ID = "selected_navigation_drawer_id"
    }
}
