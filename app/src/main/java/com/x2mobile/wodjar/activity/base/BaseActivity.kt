package com.x2mobile.wodjar.activity.base

import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.LoginActivity
import com.x2mobile.wodjar.business.network.exception.UnauthorizedException
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

open class BaseActivity : BaseToolbarActivity() {

    protected fun handleRequestFailure(throwable: Throwable?) = if (throwable is UnauthorizedException) {
        EventBus.getDefault().unregister(this)
        toast(R.string.login_expired)
        startActivity(intentFor<LoginActivity>())
        finish()
    } else {
        toast(R.string.error_occurred)
    }
}