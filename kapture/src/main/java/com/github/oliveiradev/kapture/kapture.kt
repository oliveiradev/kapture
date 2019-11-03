package com.github.oliveiradev.kapture

import android.content.Intent
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity
import androidx.work.WorkManager
import com.github.oliveiradev.kapture.Constants.KAPTURE_FRAGMENT_TAG
import java.util.UUID

typealias KaptureResultListener = KaptureResult.() -> Unit
typealias OnWorkerIdListener = (UUID) -> Unit

fun FragmentActivity.takePhoto(block: KaptureResultListener) {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val workManager = WorkManager.getInstance(applicationContext)
    val kaptureResultObserver = KaptureResultObserver(this, workManager, block)

    val oldFragment = this
        .supportFragmentManager
        .findFragmentByTag(KAPTURE_FRAGMENT_TAG) as? ResultProcessorFragment

    val onWorkerIdListener: OnWorkerIdListener = { id ->
        kaptureResultObserver.observerById(id)
    }

    oldFragment?.also {
        it.setOnWorkerIdListener(onWorkerIdListener)
        it.setWorkManager(workManager)
    } ?: startResultProcessorFragment(intent, workManager, onWorkerIdListener)
}

private fun FragmentActivity.startResultProcessorFragment(
    intent: Intent,
    workManager: WorkManager,
    onWorkerIdListener: OnWorkerIdListener
) {
    val newFragment = ResultProcessorFragment.newInstance(intent).apply {
        setWorkManager(workManager)
        setOnWorkerIdListener(onWorkerIdListener)
    }

    this.runOnUiThread {
        this.supportFragmentManager
            .beginTransaction()
            .add(newFragment, KAPTURE_FRAGMENT_TAG)
            .commitNowAllowingStateLoss()
    }
}