package com.x2mobile.wodjar.fragments.base

import android.support.v4.app.Fragment
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.LoginActivity
import com.x2mobile.wodjar.business.callback.ToolbarDelegate
import com.x2mobile.wodjar.business.network.exception.UnauthorizedException
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

open class BaseFragment : Fragment() {

    val toolbarDelegate : ToolbarDelegate by lazy { activity as ToolbarDelegate }

    protected fun handleRequestFailure(throwable: Throwable?) {
        if (throwable is UnauthorizedException) {
            EventBus.getDefault().unregister(this)
            context.toast(R.string.login_expired)
            startActivity(context.intentFor<LoginActivity>())
            activity.finish()
        } else {
            context.toast(R.string.error_occurred)
        }
    }

}