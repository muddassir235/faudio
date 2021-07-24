package com.muddassir.faudio

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.muddassir.faudio.FocusManager.FocusAudioAction.*
import com.muddassir.faudio.downloads.AudioDownloads
import com.muddassir.faudio.downloads.addDownload
import com.muddassir.faudio.downloads.dependencyProvider

class Audio(private val context: Context, lifecycleOwner: LifecycleOwner? = null) {
    private val scope: CoroutineScope = lifecycleOwner?.lifecycleScope
        ?: (context as? AppCompatActivity)?.lifecycleScope ?: GlobalScope

    private var ap = AudioProducerBuilder(context).setMediaSourceFactory(
        DefaultMediaSourceFactory(dependencyProvider(context).cacheDataSourceFactory)
    ).build()

    private val audioDownloads = AudioDownloads(context, lifecycleOwner)

    private val fm = FocusManager(context) {
        when(it) {
            STOP -> ap.stop()
            PAUSE -> ap.pause()
            RESUME -> ap.resume()
        }
    }

    private val _state = MutableLiveData<ActualAudioState>()
    val state: LiveData<ActualAudioState> = _state

    init {
        trackProgress()
    }

    suspend fun setState(newState: ExpectedAudioState): Boolean {
        withContext(Dispatchers.Main) {
            val currState = audioState

            // uris
            if (newState.audios != currState.audios) {
                ap.setUris(newState.audios.map {
                    if(it.download) {
                        audioDownloads.changeState { downloadState ->
                            addDownload(downloadState, it.uri)
                        }
                    }
                    it.uri
                })
                ap.seekTo(newState.index, 0)
            }

            // stopped
            if (newState.stopped != currState.stopped) {
                if (newState.stopped) {
                    ap.release()

                    ap = AudioProducerBuilder(context).build()
                    ap.setUris(newState.audios.map { it.uri })
                    ap.seekTo(newState.index, 0)
                } else {
                    if(newState.paused) {
                        ap.pause()
                    } else {
                        ap.startCheckFocus(fm.focused)
                    }
                }
            }

            // paused
            if (newState.paused != currState.paused) {
                if(newState.paused) {
                    ap.pause()
                } else {
                    ap.startCheckFocus(fm.focused)
                }
            }

            // index, progress
            when {
                newState.index != currState.index -> ap.seekTo(newState.index, newState.progress)
                newState.progress != currState.progress -> ap.seekTo(newState.progress)
            }

            // speed
            if (newState.speed != currState.speed) {
                ap.setAudioSpeed(newState.speed)
            }
        }

        repeat(15) {
            delay(200)
            if(state.value?.equals(newState) == true) return true
        }

        if(BuildConfig.DEBUG) {
            Log.e(
                Audio::class.simpleName,
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

    fun setStateAsync(newState: ExpectedAudioState, callback: ((Boolean)->Unit)? = null) {
        flow {
            emit(setState(newState))
        }.onEach {
            callback?.invoke(it)
        }.launchIn(scope)
    }

    suspend fun changeState(action: (ActualAudioState) -> ExpectedAudioState): Boolean {
        val newState = this.state.value?.change(action) ?: return false
        return this.setState(newState)
    }

    fun changeStateAsync(action: (ActualAudioState) -> ExpectedAudioState, callback: ((Boolean)->Unit)? = null) {
        flow {
            emit(changeState(action))
        }.onEach {
            callback?.invoke(it)
        }.launchIn(scope)
    }

    private fun trackProgress() = scope.launch {
        while (true) {
            withContext(Dispatchers.Main) {
                _state.value = audioState
            }
            delay(200)
        }
    }

    private val audioState  : ActualAudioState get() {
        return ActualAudioState(
            this.ap.uris.map { uri ->
                val download = audioDownloads.state.value?.downloads?.find { it.uri == uri }

                ActualAudioItem(
                    uri,
                    download != null,
                    audioDownloads.state.value?.paused == true,
                    download?.progress ?: 0f
                )
            },
            this.ap.currentIndex,
            !this.ap.started,
            this.ap.currentPosition,
            this.ap.speed,
            this.ap.bufferedPosition,
            this.ap.duration,
            this.ap.stopped,
            this.ap.error
        )
    }

    fun release() = this.ap.release()
}