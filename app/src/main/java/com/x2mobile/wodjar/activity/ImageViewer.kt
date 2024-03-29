package com.x2mobile.wodjar.activity

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.x2mobile.wodjar.R


class ImageViewer : AppCompatActivity() {

    private val ANIMATION_DURATION = 400L

    private val END_ALPHA = 192

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_viewer)

        val uri = intent.getParcelableExtra<Uri>(KEY_URI)
        val rect = intent.getParcelableExtra<Rect>(KEY_RECT)

        val windowRect = Rect()
        window.decorView.getWindowVisibleDisplayFrame(windowRect)

        val container = findViewById<View>(R.id.container)
        val image = findViewById<ImageView>(R.id.image)

        val imageAnimator = ValueAnimator.ofInt(0, 100)
        imageAnimator.addUpdateListener { animator ->
            val value = (animator.animatedValue as Int).toFloat() / 100
            val layoutParams = image.layoutParams as FrameLayout.LayoutParams
            layoutParams.width = (rect.width() + (windowRect.width() - rect.width()) * value).toInt()
            layoutParams.height = (rect.height() + (windowRect.height() - rect.height()) * value).toInt()
            layoutParams.leftMargin = rect.left - (rect.left * value).toInt()
            layoutParams.topMargin = rect.top - (rect.top * value).toInt()
            image.requestLayout()
        }

        val backgroundAnimator = ValueAnimator.ofInt(0, END_ALPHA)
        backgroundAnimator.addUpdateListener { animator ->
            container.setBackgroundColor(Color.argb(animator.animatedValue as Int, 0, 0, 0))
        }

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(imageAnimator, backgroundAnimator)
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.duration = ANIMATION_DURATION

        Glide.with(this@ImageViewer).load(uri).apply(RequestOptions().override(windowRect.width(), windowRect.height()))
                .listener(object : RequestListener<Drawable> {

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        animatorSet.start()
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean = false
                }).into(image)

        image.setOnClickListener { finish() }
    }

    companion object {
        val KEY_URI = "uri"
        val KEY_RECT = "rect"
    }

}
