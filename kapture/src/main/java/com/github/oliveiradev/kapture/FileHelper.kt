package com.github.oliveiradev.kapture

import android.graphics.Bitmap
import android.net.Uri
import com.github.oliveiradev.kapture.Constants.KAPTURE_FILE_CHILD
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object FileHelper {

    fun writeBitmapToFile(dir: File?, bitmap: Bitmap?): Uri {
        val outputFile = createDir(dir)
        val out = FileOutputStream(outputFile)
        out.use {
            bitmap?.compress(Bitmap.CompressFormat.PNG, 0, it)
        }
        return Uri.fromFile(outputFile)
    }

    private fun createDir(dir: File?): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            .format(Date())
        val name = String.format("%s-%s.png", UUID.randomUUID().toString(), timeStamp)
        val outputDir = File(dir, KAPTURE_FILE_CHILD)
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        return File(outputDir, name)
    }
}