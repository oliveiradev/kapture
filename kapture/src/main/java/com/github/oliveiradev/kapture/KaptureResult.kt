package com.github.oliveiradev.kapture

import android.net.Uri

class KaptureResult(private val uri: Uri) {

    fun collect(block: (Uri) -> Unit) {
        block(uri)
    }
}