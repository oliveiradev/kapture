package com.github.oliveiradev.kapture

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.github.oliveiradev.kapture.Constants.EXTRA_WORKER_URI_RESULT
import java.util.UUID

class KaptureResultObserver(
    private val owner: LifecycleOwner,
    private val workManager: WorkManager,
    private val kaptureResultListener: KaptureResultListener
) : LifecycleObserver {

    private lateinit var outputWorkInfos: LiveData<WorkInfo>

    init {
        owner.lifecycle.addObserver(this)
    }

    fun observerById(id: UUID) {
        outputWorkInfos = workManager.getWorkInfoByIdLiveData(id)
        outputWorkInfos.observe(owner, workInfosObserver())
    }

    private fun workInfosObserver(): Observer<WorkInfo> {
        return Observer { workInfo ->
            workInfo?.run {
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    val uri = workInfo.outputData.getString(EXTRA_WORKER_URI_RESULT)
                    kaptureResultListener.invoke(KaptureResult(Uri.parse(uri)))
                }
            }
        }
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        owner.lifecycle.removeObserver(this)
    }
}