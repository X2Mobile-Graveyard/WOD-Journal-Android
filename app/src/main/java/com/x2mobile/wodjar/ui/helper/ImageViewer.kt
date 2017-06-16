package com.x2mobile.wodjar.ui.helper

import android.graphics.Rect
import android.net.Uri
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.x2mobile.wodjar.activity.ImageViewer
import org.jetbrains.anko.intentFor

class ImageViewer(val fragment: Fragment, val image: ImageView) {

    private val windowRect: Rect by lazy {
        val rect = Rect()
        fragment.activity.window.decorView.getWindowVisibleDisplayFrame(rect)
        rect
    }

    var imageUri: Uri? = null
        set(value) {
            field = value
            Glide.with(fragment).load(value).override(windowRect.width(), windowRect.height()).into(image)
        }

    fun popup(imageContainer: View) {
        val intent = fragment.context.intentFor<ImageViewer>()
        intent.putExtra(ImageViewer.KEY_URI, imageUri)
        intent.putExtra(ImageViewer.KEY_RECT, Rect(imageContainer.left, imageContainer.top, imageContainer.right, imageContainer.bottom))
        fragment.startActivity(intent)
    }

}