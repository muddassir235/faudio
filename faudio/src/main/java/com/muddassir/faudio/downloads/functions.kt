package com.muddassir.faudio.downloads

import android.net.Uri
import com.google.android.exoplayer2.offline.DownloadRequest

internal val actualToExpected: ((ActualDownloadItemState) -> ExpectedDownloadItemState) = {
    ExpectedDownloadItemState(it.uri)
}

internal val addDownload = { actualDownloadState: ActualDownloadState, uri: Uri ->
    if(actualDownloadState.downloads.count { it.uri == uri } > 0) {
        ExpectedDownloadState(actualDownloadState.downloads.map(actualToExpected),
            actualDownloadState.paused)
    } else {
        val expectedDownloads = actualDownloadState.downloads.map(actualToExpected).toMutableList()
        val newExpectedDownloaded = ExpectedDownloadItemState(uri = uri)

        expectedDownloads.add(newExpectedDownloaded)

        ExpectedDownloadState(expectedDownloads, actualDownloadState.paused)
    }
}

internal val stopDownload = { actualDownloadState: ActualDownloadState, uri: Uri ->
    ExpectedDownloadState(
        actualDownloadState.downloads.filter { it.uri != uri }.map(actualToExpected),
        actualDownloadState.paused
    )
}

internal val pause = { actualDownloadState: ActualDownloadState ->
    ExpectedDownloadState(actualDownloadState.downloads.map(actualToExpected),
        paused = true)
}

internal val resume = { actualDownloadState: ActualDownloadState ->
    ExpectedDownloadState(actualDownloadState.downloads.map(actualToExpected),
        paused = false)
}

internal val expectedDownloadStateToDownloadRequest:
        ((ExpectedDownloadItemState) -> DownloadRequest) = {
    DownloadRequest.Builder(it.uri.toString(), it.uri).build()
}

