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
import com.muddassir.faudio.downloads.*
import com.muddassir.faudio.downloads.AudioDownloads
import com.muddassir.faudio.downloads.addDownload
import com.muddassir.faudio.downloads.dependencyProvider
import com.muddassir.faudio.downloads.resume

class Audio(private val context: Context, lifecycleOwner: LifecycleOwner? = null) {
    private val scope: CoroutineScope = lifecycleOwner?.lifecycleScope
        ?: (context as? AppCompatActivity)?.lifecycleScope ?: GlobalScope

    private var producer = buildAudioProducer()

    private val audioDownloads = AudioDownloads(context, lifecycleOwner)
    private val focusManager = buildFocusManager()

    private val _state = MutableLiveData<ActualAudioState>()
    val state: LiveData<ActualAudioState> = _state

    init {
        trackProgress()
    }

    private fun buildAudioProducer(): AudioProducer {
        return  AudioProducerBuilder(context).setMediaSourceFactory(
            DefaultMediaSourceFactory(dependencyProvider(context).cacheDataSourceFactory)
        ).build()
    }

    private fun buildFocusManager(): FocusManager {
        return FocusManager(context) {
            when(it) {
                STOP -> producer.stop()
                PAUSE -> producer.pause()
                RESUME -> producer.resume()
            }
        }
    }

    suspend fun setState(newState: ExpectedAudioState): Boolean {
        withContext(Dispatchers.Main) {
            val currState = audioState

            // uris
            if (newState.audios != currState.audios) {
                producer.setUris(newState.audios.map { it.uri })
                producer.seekTo(newState.index, 0)
            }

            // download
            currState.audios.zip(newState.audios).forEach {
                if(!it.first.download && it.second.download) {
                    audioDownloads should ({ ds: ActualDownloadState ->
                        addDownload(ds, it.second.uri)
                    } then resume)
                }
            }

            // stopped
            if (newState.stopped != currState.stopped) {
                if (newState.stopped) {
                    producer.release()

                    producer = buildAudioProducer()
                    producer.setUris(newState.audios.map { it.uri })
                    producer.seekTo(newState.index, 0)
                } else {
                    if(newState.paused) {
                        producer.pause()
                    } else {
                        producer.startCheckFocus(focusManager.focused)
                    }
                }
            }

            // paused
            if (newState.paused != currState.paused) {
                if(newState.paused) {
                    producer.pause()
                } else {
                    producer.startCheckFocus(focusManager.focused)
                }
            }

            // index, progress
            when {
                newState.index != currState.index -> producer.seekTo(newState.index, newState.progress)
                newState.progress != currState.progress -> producer.seekTo(newState.progress)
            }

            // speed
            if (newState.speed != currState.speed) {
                producer.setAudioSpeed(newState.speed)
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

    suspend infix fun should(action: (ActualAudioState) -> ExpectedAudioState): Boolean {
        val newState = this.state.value?.change(action) ?: return false
        return this.setState(newState)
    }

    fun changeStateAsync(action: (ActualAudioState) -> ExpectedAudioState, callback: ((Boolean)->Unit)? = null) {
        flow {
            emit(should(action))
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
            this.producer.uris.map { uri ->
                val download = audioDownloads.state.value?.downloads?.find { it.uri == uri }

                ActualAudioItem(
                    uri = uri,
                    download= download != null,
                    downloadPaused = audioDownloads.state.value?.paused == true,
                    downloadProgress = download?.progress ?: 0f
                )
            },
            this.producer.currentIndex,
            !this.producer.started,
            this.producer.currentPosition,
            this.producer.speed,
            this.producer.bufferedPosition,
            this.producer.duration,
            this.producer.stopped,
            this.producer.error
        )
    }

    fun release() = this.producer.release()
}