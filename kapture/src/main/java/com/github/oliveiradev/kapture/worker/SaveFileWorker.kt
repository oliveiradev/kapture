package com.github.oliveiradev.kapture.worker

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.github.oliveiradev.kapture.Constants
import com.github.oliveiradev.kapture.Constants.EXTRA_WORKER_URI_RESULT
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class SaveFileWorker(application: Context, params: WorkerParameters) : Worker(application, params) {

    private val dateFormatter = SimpleDateFormat(
        "yyyy.MM.dd 'at' HH:mm:ss z",
        Locale.getDefault()
    )

    override fun doWork(): Result {
        val resourceUri = inputData.getString(Constants.EXTRA_WORKER_URI)
        return try {
            val resolver = applicationContext.contentResolver
            val bitmap = BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri)))
            val imageUrl = MediaStore.Images.Media.insertImage(
                resolver, bitmap, UUID.randomUUID().toString(), dateFormatter.format(Date())
            )

            Log.d("XXXXXXXXXXXXXXXX", "IMAGE: $imageUrl")

            val outputData = workDataOf(EXTRA_WORKER_URI_RESULT to imageUrl)

            Result.success(outputData)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}