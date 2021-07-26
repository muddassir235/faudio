package com.muddassir.faudio

import android.net.Uri
import com.google.android.exoplayer2.MediaItem

internal val uriToMediaItem: ((Uri) -> MediaItem) = { MediaItem.fromUri(it) }

internal val actualAudioItemToExpectedAudioItem: ((ActualAudioItem) -> ExpectedAudioItem) = {
    ExpectedAudioItem(it.uri, it.download)
}