package com.x2mobile.wodjar.fragments.base

import android.os.Bundle
import android.support.v4.app.Fragment
import com.crashlytics.android.Crashlytics
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.LoginActivity
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.business.callback.ToolbarDelegate
import com.x2mobile.wodjar.business.network.exception.ServerException
import com.x2mobile.wodjar.business.network.exception.UnauthorizedException
import com.x2mobile.wodjar.data.event.LoggedOutEvent
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast

open class BaseFragment : Fragment() {

    val toolbarDelegate: ToolbarDelegate by lazy { activity as ToolbarDelegate }

    var savedArguments: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedArguments = savedInstanceState
    }

    protected fun showLoginAlert() {
        alert(R.string.login_to_continue) {
            positiveButton(getString(R.string.login)) { startActivity(context.intentFor<LoginActivity>()) }
            negativeButton(R.string.cancel) { }
        }.show()
    }

    protected fun confirmDeleteAlert(delete: () -> Unit) {
        alert(R.string.delete_confirmation) {
            positiveButton(getString(R.string.delete)) { delete.invoke() }
            negativeButton(R.string.cancel) { }
        }.show()
    }

    protected fun handleRequestFailure(throwable: Throwable?) {
        if (throwable is UnauthorizedException) {
            Preference.clear(context)
            EventBus.getDefault().removeAllStickyEvents()
            EventBus.getDefault().post(LoggedOutEvent())
            toast(R.string.login_expired)
            startActivity(context.intentFor<LoginActivity>())
        } else if (throwable is ServerException) {
            toast(throwable.errors?.firstOrNull() ?: getString(R.string.error_occurred))
        } else {
            Crashlytics.logException(throwable)
            toast(R.string.error_occurred)
        }
    }

}