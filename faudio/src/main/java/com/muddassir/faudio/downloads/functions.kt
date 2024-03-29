package com.muddassir.faudio.downloads

import android.net.Uri

internal val addDownload = { actualDownloadState: ActualDownloadState, uri: Uri ->
    if(actualDownloadState.downloads.count { it.uri == uri } > 0 /* no change */) {
        ExpectedDownloadState(
            actualDownloadState.downloads.map(actualToExpectedItem),
            actualDownloadState.paused
        )
    } else {
        val expectedDownloads = actualDownloadState.downloads.map(actualToExpectedItem).toMutableList()
        val newExpectedDownloaded = ExpectedDownloadItemState(uri = uri)

        expectedDownloads.add(newExpectedDownloaded)

        ExpectedDownloadState(expectedDownloads, actualDownloadState.paused)
    }
}

internal val stopDownload = { actualDownloadState: ActualDownloadState, uri: Uri ->
    ExpectedDownloadState(
        actualDownloadState.downloads.filter { it.uri != uri }.map(actualToExpectedItem),
        actualDownloadState.paused
    )
}

internal val pause = { actualDownloadState: ActualDownloadState ->
    ExpectedDownloadState(actualDownloadState.downloads.map(actualToExpectedItem), paused = true)
}

internal val resume = { actualDownloadState: ActualDownloadState ->
    ExpectedDownloadState(actualDownloadState.downloads.map(actualToExpectedItem), paused = false)
}