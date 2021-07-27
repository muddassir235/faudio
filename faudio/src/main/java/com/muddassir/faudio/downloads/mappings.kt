package com.muddassir.faudio.downloads

import com.google.android.exoplayer2.offline.DownloadRequest

internal val expectedDownloadStateToDownloadRequest:
        ((ExpectedDownloadItemState) -> DownloadRequest) = {
    DownloadRequest.Builder(it.uri.toString(), it.uri).build()
}

internal val actualToExpectedItem: ((ActualDownloadItemState) -> ExpectedDownloadItemState) = {
    ExpectedDownloadItemState(it.uri)
}

internal val expectedToActualState: ((ExpectedDownloadState) -> ActualDownloadState) = {
    ActualDownloadState(it.downloads.map { ActualDownloadItemState(it.uri, 0.0f) },
        it.paused)
}