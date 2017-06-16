package com.x2mobile.wodjar.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.base.BaseFormActivity
import com.x2mobile.wodjar.business.network.AmazonService
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.SignUpRequestEvent
import com.x2mobile.wodjar.data.event.SignUpRequestFailureEvent
import com.x2mobile.wodjar.data.model.User
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast

class SignUpActivity : BaseFormActivity() {

    var user: User? = null

    var imageUri: Uri? = null

    val name: EditText by lazy {
        findViewById(R.id.name) as EditText
    }

    val image: ImageView by lazy {
        findViewById(R.id.image) as ImageView
    }

    var progress: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        title = getString(R.string.register)

        image.onClick {
            CropImage.activity(null).setCropShape(CropImageView.CropShape.RECTANGLE).setAspectRatio(1, 1)
                    .setGuidelines(CropImageView.Guidelines.ON).start(this)
        }

        val register = findViewById(R.id.register)
        register.onClick {
            if (isInputValid()) {
                progress = indeterminateProgressDialog(R.string.registering)
                user = User(email.text.toString(), password.text.toString(), name.text.toString())
                doAsync {
                    if (imageUri != null) {
                        AmazonService.upload(this, imageUri!!, email.text.toString(), { uri ->
                            user!!.imageUri = uri
                        })
                    }

                    Service.signUp(user!!)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(intent)
            if (resultCode == Activity.RESULT_OK) {
                imageUri = result.uri
                Glide.with(this).load(imageUri).fitCenter().into(image)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                toast(result.error.message!!)
            }
        }
    }

    override fun isInputValid(): Boolean {
        if (!isInputValid(name)) {
            return false
        }
        return super.isInputValid()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSignUpResponse(event: SignUpRequestEvent) {
        progress?.dismiss()

        val data = Intent()
        data.putExtra(KEY_USER, user)
        setResult(Activity.RESULT_OK, data)
        finish()
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
