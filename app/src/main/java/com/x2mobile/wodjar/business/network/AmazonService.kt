package com.x2mobile.wodjar.business.network

import android.net.Uri
import android.support.v4.app.Fragment
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.PutObjectResult
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.WodJarApplication
import com.x2mobile.wodjar.business.Constants
import com.x2mobile.wodjar.business.FileUtil
import com.x2mobile.wodjar.business.Preference
import org.jetbrains.anko.AnkoAsyncContext
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

object AmazonService {

    val amazonS3: AmazonS3Client by lazy {
        AmazonS3Client(CognitoCachingCredentialsProvider(WodJarApplication.INSTANCE, Constants.IDENTIFY_POLL_ID, Regions.EU_WEST_2))
    }

    fun upload(fileName: String, imageUri: Uri): PutObjectResult? {
        val data = FileUtil.prepareForUpload(WodJarApplication.INSTANCE, imageUri)
        if (data != null && data.second > 0) {
            val metadata = ObjectMetadata()
            metadata.contentLength = data.second
            return amazonS3.putObject(PutObjectRequest(Constants.BUCKET, fileName, data.first, metadata))
        }
        return null
    }

    fun <T : Fragment> upload(asyncContext: AnkoAsyncContext<T>, uri: Uri, success: (Uri) -> Unit) {
        val fileName = Constants.IMAGE_NAME.format(Preference.getUserId(WodJarApplication.INSTANCE.applicationContext), System.currentTimeMillis())
        val response = upload(fileName, uri)
        if (response != null) {
            success(Uri.parse(Constants.BUCKET_IMAGE_URL.format(fileName)))
        } else {
            asyncContext.uiThread { fragment ->
                fragment.context.toast(R.string.image_upload_failed)
            }
        }
    }

}