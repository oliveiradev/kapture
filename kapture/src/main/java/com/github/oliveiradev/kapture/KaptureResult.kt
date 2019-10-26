package com.github.oliveiradev.kapture

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.IOException

class KaptureResult(private val result: Intent?) {

    fun collectAsBitmap(block: (Bitmap?, Throwable?) -> Unit) {
        if (result?.hasExtra(BITMAP_EXTRA) == true) {
            val bitmap = result.extras?.get(BITMAP_EXTRA) as Bitmap
            block(bitmap, null)
        } else {
            block(null, IllegalStateException("Has no bitmap into intent result! ${result?.extras}"))
        }
    }

    fun saveAndCollectAsBitmap(context: Context, block: (Bitmap?, Throwable?) -> Unit) {
        try {
            val storage = result?.extras?.get(STORAGE_EXTRA) as File
            val file = FileHelper.createImageFile(storage)
            addToGallery(context, file)
            collectAsBitmap(block)
        } catch (e: IOException) {
            block(null, IllegalStateException("Was no possible to save image ${e.message}"))
        }
    }

    private fun addToGallery(context: Context, file: File) {
        val mediaScanIntent = Intent().apply {
            data = Uri.fromFile(file)
        }
        context.sendBroadcast(mediaScanIntent)
    }

    companion object {

        private const val BITMAP_EXTRA = "data"
        private const val STORAGE_EXTRA = "storage"
    }
}