package com.muddassir.faudio

import android.content.Context
import android.media.AudioManager
import android.media.AudioManager.*
import com.muddassir.kmacros.delay

internal class FocusManager(context: Context, private val performAudioAction: (FocusAudioAction) -> Unit) {
    enum class FocusAudioAction { STOP, PAUSE, RESUME }

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private var _focused = false

    val focused get() = _focused

    private val focusChangeListener = OnAudioFocusChangeListener { focusChange ->
        when(focusChange) {
            AUDIOFOCUS_LOSS, AUDIOFOCUS_LOSS_TRANSIENT, AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ->
                performAudioAction(FocusAudioAction.PAUSE)
            AUDIOFOCUS_GAIN -> performAudioAction(FocusAudioAction.RESUME)
        }

        if(focusChange == AUDIOFOCUS_LOSS) {
            delay(30) { if(!_focused) performAudioAction(FocusAudioAction.STOP) }
        }

        _focused = focusChange == AUDIOFOCUS_GAIN
    }

    init {
        val result = audioManager.requestFocus(focusChangeListener)

        _focused = when(result) {
            AUDIOFOCUS_REQUEST_FAILED -> {
                performAudioAction(FocusAudioAction.STOP)
                false
            }
            AUDIOFOCUS_REQUEST_GRANTED -> {
                performAudioAction(FocusAudioAction.RESUME)
                true
            }
            AUDIOFOCUS_REQUEST_DELAYED -> {
                performAudioAction(FocusAudioAction.PAUSE)
                false
            }
            else -> false
        }
    }
}