package com.muddassir.faudio

import android.media.AudioManager
import androidx.media.AudioAttributesCompat
import androidx.media.AudioFocusRequestCompat
import androidx.media.AudioManagerCompat

internal fun AudioManager.requestFocus(focusChangeListener: AudioManager.OnAudioFocusChangeListener)
        : Int {
    val result = AudioManagerCompat.requestAudioFocus(
        this,
        AudioFocusRequestCompat.Builder(AudioManagerCompat.AUDIOFOCUS_GAIN).run {
            setAudioAttributes(AudioAttributesCompat.Builder().run {
                setUsage(AudioAttributesCompat.USAGE_MEDIA)
                setContentType(AudioAttributesCompat.CONTENT_TYPE_SPEECH)
                build()
            })

            setWillPauseWhenDucked(true)
            setOnAudioFocusChangeListener(focusChangeListener)
            build()
        }
    )

    return result
}