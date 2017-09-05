package com.x2mobile.wodjar.ui.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class ResizeAnimation(private val view: View, private val startHeight: Int, private val targetHeight: Int,
                      private val startWidth: Int, private val targetWidth: Int) : Animation() {

    override fun applyTransformation(interpolatedTime: Float, transformation: Transformation) {
        view.layoutParams.height = (startHeight + (targetHeight - startHeight) * interpolatedTime).toInt()
        view.layoutParams.width = (startWidth + (targetWidth - startWidth) * interpolatedTime).toInt()
        view.requestLayout()
    }

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) = super.initialize(startWidth, startHeight, parentWidth, parentHeight)

    override fun willChangeBounds(): Boolean = true
}
