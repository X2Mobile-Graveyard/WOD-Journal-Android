package com.x2mobile.wodjar.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.base.BaseFormActivity
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.SignUpRequestEvent
import com.x2mobile.wodjar.data.event.SignUpRequestFailureEvent
import com.x2mobile.wodjar.data.model.User
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast

class SignUpActivity : BaseFormActivity() {

    var user: User? = null

    var progress: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        title = getString(R.string.register)

        val register = findViewById(R.id.register)
        register.onClick {
            if (isInputValid()) {
                user = User(username.text.toString(), password.text.toString())
                progress = indeterminateProgressDialog(R.string.registering)
                Service.signUp(user!!)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSignUpResponse(event: SignUpRequestEvent) {
        progress?.dismiss()
        if (event.response?.isSuccessful ?: false) {
            val data = Intent()
            data.putExtra(KEY_USER, user)
            setResult(Activity.RESULT_OK, data)
            finish()
        } else {
            toast(R.string.error_occurred)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSignUpFailed(event: SignUpRequestFailureEvent) {
        progress?.dismiss()
        toast(R.string.error_occurred)
    }

    companion object {
        val KEY_USER = "user"
    }
}
