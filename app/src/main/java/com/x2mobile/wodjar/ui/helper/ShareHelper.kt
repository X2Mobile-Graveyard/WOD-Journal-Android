package com.x2mobile.wodjar.ui.helper

import android.graphics.*
import android.support.v4.app.Fragment
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.facebook.share.model.ShareMediaContent
import com.facebook.share.model.SharePhoto
import com.facebook.share.widget.ShareDialog

class ShareHelper(val fragment: Fragment) {

    val SHARE_IMAGE_WIDTH = 480

    val SHARE_TEXT_SIZE = 14f

    fun share(text: String, image: Bitmap?) {
        val paint = TextPaint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.textSize = SHARE_TEXT_SIZE
        paint.color = Color.BLACK

        val textLayout = StaticLayout(text, paint, SHARE_IMAGE_WIDTH, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)

        val overlay = Bitmap.createBitmap(SHARE_IMAGE_WIDTH, textLayout.height, Bitmap.Config.RGB_565)
        val canvas = Canvas(overlay)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC)
        textLayout.draw(canvas)

        val photo = SharePhoto.Builder().setBitmap(overlay).setUserGenerated(true).build()
        val contentBuilder = ShareMediaContent.Builder().addMedium(photo)
        if (image != null) {
            contentBuilder.addMedium(SharePhoto.Builder().setBitmap(image).setUserGenerated(true).build())
        }

        ShareDialog.show(fragment, contentBuilder.build())
    }

}