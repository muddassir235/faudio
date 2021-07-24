package com.muddassir.faudio.downloads

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.muddassir.faudio.BuildConfig
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class AudioDownloads(context: Context, lifecycleOwner: LifecycleOwner? = null) {
    private val scope: CoroutineScope = lifecycleOwner?.lifecycleScope
        ?: (context as? AppCompatActivity)?.lifecycleScope ?: GlobalScope

    private val dependencies = dependencyProvider(context)

    private val downloadManager = DownloadManager(context,
        dependencies.databaseProvider,
        dependencies.downloadCache,
        dependencies.dataSourceFactory,
        dependencies.downloadExecutor
    ).apply {
        maxParallelDownloads = 3
    }

    private val manager: DownloadManager = downloadManager

    private val _state = MutableLiveData(ActualDownloadState(emptyList(), false))
    val state: LiveData<ActualDownloadState> = _state

    init {
        trackProgress()
    }

    suspend fun setState(newState: ExpectedDownloadState): Boolean {
        val actualDownloads = state.value?.downloads ?: emptyList()
        val expectedDownloads = newState.downloads

        expectedDownloads.forEach { state ->
            val existingDownload = actualDownloads.find { actualDownloadState ->
                actualDownloadState.uri == state.uri
            }

            if(existingDownload == null) {
                manager.addDownload(expectedDownloadStateToDownloadRequest(state))
            }
        }

        actualDownloads.forEach { state ->
            if(expectedDownloads.find { actualDownloadState ->
                actualDownloadState.uri == state.uri
            } == null) {
                manager.setStopReason(state.uri.toString(), Download.STATE_REMOVING)
            }
        }

        if(newState.paused) {
            manager.pauseDownloads()
        } else {
            manager.resumeDownloads()
        }

        repeat(15) {
            delay(200)
            if(state.value?.equals(newState) == true) return true
        }

        if(BuildConfig.DEBUG) {
            Log.e(
                AudioDownloads::class.simpleName,
                """
                Unmatched states: 
                
                *********
                actual   : ${state.value}
                expected : $newState
                *********
                """.trimIndent()
            )
        }

        return state.value?.equals(newState) == true
    }

    fun setStateAsync(newState: ExpectedDownloadState, callback: ((Boolean)->Unit)? = null) {
        flow {
            emit(setState(newState))
        }.onEach {
            callback?.invoke(it)
        }.launchIn(scope)
    }

    suspend fun changeState(action: (ActualDownloadState) -> ExpectedDownloadState): Boolean {
        val newState = this.state.value?.change(action) ?: return false
        return this.setState(newState)
    }

    fun changeStateAsync(action: (ActualDownloadState) -> ExpectedDownloadState,
                         callback: ((Boolean)->Unit)? = null) {
        flow {
            emit(changeState(action))
        }.onEach {
            callback?.invoke(it)
        }.launchIn(scope)
    }

    private fun trackProgress() = scope.launch {
        while (true) {
            withContext(Dispatchers.Main) {
                _state.value = manager.downloadState
            }
            delay(200)
        }
    }
}