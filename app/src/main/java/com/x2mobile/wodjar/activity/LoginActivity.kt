package com.x2mobile.wodjar.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import com.classlink.analytics.business.Preference
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.base.BaseFormActivity
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.LoggedInEvent
import com.x2mobile.wodjar.data.event.LoginRequestEvent
import com.x2mobile.wodjar.data.event.LoginRequestFailureEvent
import com.x2mobile.wodjar.data.model.User
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast

class LoginActivity : BaseFormActivity(), FacebookCallback<LoginResult> {

    val PERMISSION_EMAIL = "email"

    val REQUEST_CODE_REGISTER = 1

    val callbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }

    var progress: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        title = getString(R.string.login)

        val login = findViewById(R.id.login)
        login.onClick {
            if (isInputValid()) {
                progress = indeterminateProgressDialog(R.string.logging)
                Service.login(User(username.text.toString(), password.text.toString()))
            }
        }

        val register = findViewById(R.id.register)
        register.onClick {
            startActivityForResult(intentFor<SignUpActivity>(), REQUEST_CODE_REGISTER)
        }

        val facebookLogin = findViewById(R.id.facebook_login) as LoginButton
        facebookLogin.setReadPermissions(PERMISSION_EMAIL)
        facebookLogin.registerCallback(callbackManager, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_REGISTER) {
            if (resultCode == Activity.RESULT_OK) {
                progress = indeterminateProgressDialog(R.string.logging)
                val user = data?.getParcelableExtra<User>(SignUpActivity.KEY_USER)
                Service.login(user!!)
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSuccess(result: LoginResult) {
        Service.login(result.accessToken.token)
    }

    override fun onError(error: FacebookException?) {
        toast(R.string.error_occurred)
    }

    override fun onCancel() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginResponse(event: LoginRequestEvent) {
        progress?.dismiss()
        if (event.response?.isSuccessful ?: false) {
            val loginResponse = event.response!!.body()
            if (loginResponse.errors == null) {
                Preference.setUserId(this, loginResponse.userId)
                Preference.setToken(this, loginResponse.authToken)
                Preference.setDisplayName(this, username.text.toString())
                EventBus.getDefault().post(LoggedInEvent())
                finish()
            } else {
                toast(loginResponse.errors!!.first())
            }
        } else {
            toast(R.string.error_occurred)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginFailed(event: LoginRequestFailureEvent) {
        progress?.dismiss()
        toast(R.string.error_occurred)
    }

}