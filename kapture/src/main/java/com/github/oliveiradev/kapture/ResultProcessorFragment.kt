package com.github.oliveiradev.kapture

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

typealias KaptureResultListener = (KaptureResult) -> Unit

class ResultProcessorFragment : Fragment() {

    private var kaptureResultListener: KaptureResultListener? = null
    private var intentExtra: Intent? = null

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

        if (requestCode == KAPTURE_IMAGE_REQUEST) {
            kaptureResultListener?.invoke(KaptureResult(data))
            removeFragment()
        }
    }

    fun setKaptureResultListener(kaptureResultListener: KaptureResultListener)  {
        this.kaptureResultListener = kaptureResultListener
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