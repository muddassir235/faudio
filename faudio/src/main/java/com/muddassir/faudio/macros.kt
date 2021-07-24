package com.muddassir.faudio

import android.media.AudioManager
import android.net.Uri
import androidx.media.AudioAttributesCompat
import androidx.media.AudioFocusRequestCompat
import androidx.media.AudioManagerCompat

/**
 * Converts ArrayList of string uris/urls/file paths to Uris
 */
internal fun ArrayList<String>.toUris(): Array<Uri> {
    return Array(size) { Uri.parse(get(it)) }
}

/**
 * Converts Array of string uris/urls/file paths to Uris
 */
internal fun Array<String>.toUris(): Array<Uri> {
    return Array(size) { Uri.parse(this[it]) }
}

/**
 * Converts string uris/urls/file paths arguments to Uris
 */
fun uris(vararg uriStrings: String): List<Uri> {
    return uriStrings.map { Uri.parse(it) }
}

fun audios(vararg uriStrings: String): List<ActualAudioItem> {
    return Array(uriStrings.size) { Uri.parse(uriStrings[it]) }.map {
        ActualAudioItem(it, false, false, 0f)
    }
}

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