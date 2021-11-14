package com.muddassir.faudio

import android.net.Uri
import com.google.android.exoplayer2.MediaItem

internal val uriToMediaItem: ((Uri) -> MediaItem) = { MediaItem.fromUri(it) }

internal val actualToExpectedItem: ((ActualAudioItem) -> ExpectedAudioItem) = {
    ExpectedAudioItem(it.uri, it.download)
}

val expectedToActualState: (ExpectedAudioState) -> ActualAudioState = {
    ActualAudioState(
        it.items.map { item -> ActualAudioItem(item.uri, item.download, false, 0.0f) },
        it.index,
        it.paused,
        false,
        it.progress,
        it.speed,
        0L,
        0L,
        it.stopped,
        null
    )
}