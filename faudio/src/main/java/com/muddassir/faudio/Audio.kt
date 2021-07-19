package com.muddassir.faudio

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.muddassir.faudio.FocusManager.FocusAudioAction.*

class Audio(private val context: Context, lifecycleOwner: LifecycleOwner? = null) {
    private val scope: CoroutineScope = lifecycleOwner?.lifecycleScope
        ?: (context as? AppCompatActivity)?.lifecycleScope ?: GlobalScope

    private var ap = AudioProducerBuilder(context).build()
    private val fm = FocusManager(context) {
        when(it) {
            STOP -> ap.stop()
            PAUSE -> ap.pause()
            RESUME -> ap.resume()
        }
    }

    private val _state = MutableLiveData<ActualState>()
    val state: LiveData<ActualState> = _state

    init {
        trackProgress()
    }

    suspend fun setState(newState: ExpectedState): Boolean {
        withContext(Dispatchers.Main) {
            val currState = ap.audioState

            // uris
            if (!newState.uris.contentEquals(currState.uris)) {
                ap.setUris(newState.uris)
                ap.seekTo(newState.index, 0)
            }

            // stopped
            if (newState.stopped != currState.stopped) {
                if (newState.stopped) {
                    ap.release()

                    ap = AudioProducerBuilder(context).build()
                    ap.setUris(newState.uris)
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

    fun setStateAsync(newState: ExpectedState, callback: ((Boolean)->Unit)? = null) {
        flow {
            emit(setState(newState))
        }.onEach {
            callback?.invoke(it)
        }.launchIn(scope)
    }

    suspend fun changeState(action: (ActualState) -> ExpectedState): Boolean {
        val newState = this.state.value?.change(action) ?: return false
        return this.setState(newState)
    }

    fun changeStateAsync(action: (ActualState) -> ExpectedState, callback: ((Boolean)->Unit)? = null) {
        flow {
            emit(changeState(action))
        }.onEach {
            callback?.invoke(it)
        }.launchIn(scope)
    }

    private fun trackProgress() = scope.launch {
        while (true) {
            withContext(Dispatchers.Main) {
                _state.value = ap.audioState
            }
            delay(200)
        }
    }

    fun release() = this.ap.release()
}