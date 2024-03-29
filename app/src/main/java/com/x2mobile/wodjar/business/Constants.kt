package com.x2mobile.wodjar.business

import com.x2mobile.wodjar.BuildConfig

object Constants {

    const val ID_NA = -1

    const val IDENTIFY_POLL_ID = "eu-west-2:a6b0223a-0d4f-41c2-903f-daf2b9097fb0"

    const val BUCKET = "workoutoftheday-images"
    const val BUCKET_IMAGE_URL = "https://s3.eu-west-2.amazonaws.com/$BUCKET/%s"

    const val YOUTUBE_API_KEY = "AIzaSyB34lB_4J82Bbv73MXQPRPOhBvvwdU77Xk"

    const val IMAGE_NAME = "%d_%d"

    const val FILE_AUTHORITY = "${BuildConfig.APPLICATION_ID}.android.provider"

    const val CAMERA_IMAGE_NAME = "result_%d.jpeg"
}