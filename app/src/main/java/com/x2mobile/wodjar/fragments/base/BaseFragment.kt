package com.x2mobile.wodjar.fragments.base

import android.os.Bundle
import android.support.v4.app.Fragment
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.LoginActivity
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.business.callback.ToolbarDelegate
import com.x2mobile.wodjar.business.network.exception.UnauthorizedException
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

open class BaseFragment : Fragment() {

    val toolbarDelegate: ToolbarDelegate by lazy { activity as ToolbarDelegate }

    var savedArguments: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedArguments = savedInstanceState
    }

    protected fun showLoginAlert() {
        context.alert(R.string.login_to_continue) {
            positiveButton(getString(R.string.login)) { startActivity(context.intentFor<LoginActivity>()) }
            cancelButton { }
        }.show()
    }

    protected fun handleRequestFailure(throwable: Throwable?) {
        if (throwable is UnauthorizedException) {
            EventBus.getDefault().unregister(this)
            Preference.clear(context)
            context.toast(R.string.login_expired)
            startActivity(context.intentFor<LoginActivity>())
        } else {
            context.toast(R.string.error_occurred)
        }
    }

}