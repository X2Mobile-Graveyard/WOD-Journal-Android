package com.x2mobile.wodjar.ui.helper

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.data.event.ImageSetEvent
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.longToast

class ImagePicker(val fragment: Fragment) {

    private val REQUEST_CODE_PICK_IMAGE = 13

    private val REQUEST_CODE_STORAGE = 97

    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?): Boolean {
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK && intent != null) {
                EventBus.getDefault().post(ImageSetEvent(intent.data))
            }
            return true
        }
        return false
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean {
        if (requestCode == REQUEST_CODE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addPictureInternal()
            } else {
                fragment.context.longToast(R.string.storage_permission_denied)
            }
            return true
        }
        return false
    }

    fun addPicture(fragment: Fragment) {
        if (ContextCompat.checkSelfPermission(fragment.context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            addPictureInternal()
        } else {
            fragment.requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_STORAGE)
        }
    }

    private fun addPictureInternal() {
        val pickIntent = Intent(Intent.ACTION_GET_CONTENT)
        pickIntent.addCategory(Intent.CATEGORY_OPENABLE)
        pickIntent.type = "image/*"
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        fragment.startActivityForResult(Intent.createChooser(pickIntent, fragment.getString(R.string.add_picture))
                .putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(takePhotoIntent)), REQUEST_CODE_PICK_IMAGE)
    }
}
