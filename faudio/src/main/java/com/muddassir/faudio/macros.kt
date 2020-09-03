package com.muddassir.faudio

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import androidx.media.AudioAttributesCompat
import androidx.media.AudioFocusRequestCompat
import androidx.media.AudioManagerCompat
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util

/**
 * Converts ArrayList of string uris/urls/file paths to Uris
 */
fun ArrayList<String>.toUris(): Array<Uri> {
    return Array(size) { Uri.parse(get(it)) }
}

/**
 * Converts Array of string uris/urls/file paths to Uris
 */
fun Array<String>.toUris(): Array<Uri> {
    return Array(size) { Uri.parse(this[it]) }
}

/**
 * Converts string uris/urls/file paths arguments to Uris
 */
fun uris(vararg uriStrings: String): Array<Uri> {
    return Array(uriStrings.size) { Uri.parse(uriStrings[it]) }
}

internal fun AudioManager.gainFocus(focusChangeListener: AudioManager.OnAudioFocusChangeListener)
        : Boolean {
    val result = AudioManagerCompat.requestAudioFocus(
        this,
        AudioFocusRequestCompat.Builder(AudioManagerCompat.AUDIOFOCUS_GAIN)
            .setAudioAttributes(
                AudioAttributesCompat.Builder()
                    .setUsage(AudioAttributesCompat.USAGE_MEDIA)
                    .setContentType(AudioAttributesCompat.CONTENT_TYPE_SPEECH)
                    .build())
            .setWillPauseWhenDucked(true)
            .setOnAudioFocusChangeListener(focusChangeListener).build()
    )

    return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
}

internal fun mediaSourceFromUrls(c: Context, urls: Array<Uri>): MediaSource {
    val dsf = DefaultHttpDataSourceFactory(Util.getUserAgent(c, "faudio"))
    val ms = MediaSource()
    urls.forEach { ms.addMediaSource(ProgressiveMediaSource.Factory(dsf).createMediaSource(it)) }

    return ms
}
