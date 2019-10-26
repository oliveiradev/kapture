package com.github.oliveiradev.kapture

import android.content.Intent
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity

private const val KAPTURE_FRAGMENT_TAG = "FragmentProcessorTAG"

typealias KaptureResultListener = KaptureResult.() -> Unit

fun FragmentActivity.takePhoto(block: KaptureResultListener) {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val oldFragment = this
        .supportFragmentManager
        .findFragmentByTag(KAPTURE_FRAGMENT_TAG) as? ResultProcessorFragment

    oldFragment?.also {
        it.setKaptureResultListener(block)
    } ?: startResultProcessorFragment(intent, block)
}

private fun FragmentActivity.startResultProcessorFragment(
    intent: Intent,
    block: KaptureResultListener
) {
    val newFragment = ResultProcessorFragment.newInstance(intent)
    newFragment.setKaptureResultListener(block)

    this.runOnUiThread {
        this.supportFragmentManager
            .beginTransaction()
            .add(newFragment, KAPTURE_FRAGMENT_TAG)
            .commitNowAllowingStateLoss()
    }
}