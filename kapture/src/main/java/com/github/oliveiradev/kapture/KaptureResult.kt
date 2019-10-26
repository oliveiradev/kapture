package com.github.oliveiradev.kapture

import android.content.Intent
import android.graphics.Bitmap

class KaptureResult(private val result: Intent?) {

    fun collectAsBitmap(block: (Bitmap?, Throwable?) -> Unit) {
        if (result?.hasExtra(BITMAP_EXTRA) == true) {
            val bitmap = result.extras?.get(BITMAP_EXTRA) as Bitmap
            block(bitmap, null)
        } else {
            block(null, IllegalStateException("Has no bitmap into intent result! ${result?.extras}"))
        }
    }

    companion object {

        private const val BITMAP_EXTRA = "data"
    }
}