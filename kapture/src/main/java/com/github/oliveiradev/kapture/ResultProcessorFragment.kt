package com.github.oliveiradev.kapture

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.github.oliveiradev.kapture.Constants.KAPTURE_FRAGMENT_TAG
import com.github.oliveiradev.kapture.worker.SaveFileWorker
import java.util.UUID

typealias WorkerId = UUID

class ResultProcessorFragment : Fragment() {

    private var intentExtra: Intent? = null
    private var workManager: WorkManager? = null
    private var onWorkerIdListener: OnWorkerIdListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<Intent>(KAPTURE_INTENT_EXTRA)?.let {
            intentExtra = it
        }
    }

    override fun onResume() {
        super.onResume()
        intentExtra?.run(::processIntent) ?: removeFragment()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == KAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val dir = context?.applicationContext?.filesDir
            val bitmap = data?.extras?.get("data") as? Bitmap
            val uri = FileHelper.writeBitmapToFile(dir, bitmap)
            val workerId = startWorkers(uri)
            onWorkerIdListener?.invoke(workerId)
        }
        removeFragment()
    }

    fun setWorkManager(workManager: WorkManager) {
        this.workManager = workManager
    }

    fun setOnWorkerIdListener(onWorkerIdListener: OnWorkerIdListener) {
        this.onWorkerIdListener = onWorkerIdListener
    }

    private fun processIntent(intent: Intent) {
        intent.also { takePictureIntent ->
            startActivityForResult(takePictureIntent, KAPTURE_IMAGE_REQUEST)
        }
    }

    private fun removeFragment() {
        fragmentManager?.beginTransaction()
            ?.remove(this)
            ?.commitAllowingStateLoss()
    }

    private fun startWorkers(uri: Uri): WorkerId {
        val saveFileWorker = OneTimeWorkRequestBuilder<SaveFileWorker>()
            .addTag(KAPTURE_FRAGMENT_TAG)
            .setInputData(
                Data.Builder()
                    .putString(Constants.EXTRA_WORKER_URI, uri.toString())
                    .build()
            )
            .build()

        workManager?.enqueueUniqueWork(
            KAPTURE_FRAGMENT_TAG,
            ExistingWorkPolicy.REPLACE,
            saveFileWorker
        )

        return saveFileWorker.id
    }

    companion object {

        private const val KAPTURE_IMAGE_REQUEST = 1
        private const val KAPTURE_INTENT_EXTRA = "KAPTURE_INTENT_EXTRA"

        fun newInstance(intent: Intent): ResultProcessorFragment {
            return ResultProcessorFragment().apply {
                arguments = Bundle().also {
                    it.putParcelable(KAPTURE_INTENT_EXTRA, intent)
                }
            }
        }
    }
}