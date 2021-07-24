package com.muddassir.faudio.downloads

import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager

internal val DownloadManager.downloadState: ActualDownloadState get() {
    val downloadsList = ArrayList<ActualDownloadItemState>()

    val cursor = this.downloadIndex.getDownloads(
        Download.STATE_QUEUED,
        Download.STATE_DOWNLOADING,
        Download.STATE_COMPLETED,
        Download.STATE_RESTARTING
    )

    for(i in 0 until cursor.count) {
        cursor.moveToPosition(i)

        downloadsList.add(
            ActualDownloadItemState(cursor.download.request.uri, cursor.download.percentDownloaded)
        )
    }
    return ActualDownloadState(downloadsList.toTypedArray(), this.downloadsPaused)
}