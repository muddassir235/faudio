package com.muddassir.faudio.downloads

import android.net.Uri
internal data class ActualDownloadItemState(
    val uri: Uri,
    val progress: Float
) {
    override fun equals(other: Any?): Boolean {
        return when(other) {
            is ExpectedDownloadItemState -> this.uri == other.uri
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = uri.hashCode()
        result = 31 * result + progress.hashCode()
        return result
    }
}

internal data class ExpectedDownloadItemState(
    val uri: Uri
) {
    override fun equals(other: Any?): Boolean {
        return when(other) {
            is ActualDownloadItemState -> this.uri == other.uri
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return uri.hashCode()
    }
}

internal data class ActualDownloadState(
    val downloads: List<ActualDownloadItemState>,
    val paused: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ActualDownloadState

        if (downloads != other.downloads) return false
        if (paused != other.paused) return false

        return true
    }

    override fun hashCode(): Int {
        var result = downloads.hashCode()
        result = 31 * result + paused.hashCode()
        return result
    }

    fun change(action: (ActualDownloadState) -> ExpectedDownloadState): ExpectedDownloadState {
        return action(this)
    }
}

internal data class ExpectedDownloadState(
    val downloads: List<ExpectedDownloadItemState>,
    val paused: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExpectedDownloadState

        if (downloads != other.downloads) return false
        if (paused != other.paused) return false

        return true
    }

    override fun hashCode(): Int {
        var result = downloads.hashCode()
        result = 31 * result + paused.hashCode()
        return result
    }
}