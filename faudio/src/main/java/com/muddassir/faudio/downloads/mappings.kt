package com.muddassir.faudio.downloads

import com.google.android.exoplayer2.offline.DownloadRequest

internal val expectedDownloadStateToDownloadRequest:
        ((ExpectedDownloadItemState) -> DownloadRequest) = {
    DownloadRequest.Builder(it.uri.toString(), it.uri).build()
}

internal val actualToExpected: ((ActualDownloadItemState) -> ExpectedDownloadItemState) = {
    ExpectedDownloadItemState(it.uri)
}