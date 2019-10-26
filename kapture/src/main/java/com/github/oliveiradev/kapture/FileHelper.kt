package com.github.oliveiradev.kapture

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileHelper {

    @Throws(IOException::class)
    fun createImageFile(storageDir: File): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            .format(Date())
        return File.createTempFile("JPEG_${timeStamp}_",".jpg", storageDir)
    }
}