package com.muddassir.faudio.downloads

import android.net.Uri

internal data class ActualDownloadItemState(val uri: Uri, val progress: Float) {
    override fun equals(other: Any?): Boolean {
        return when(other) {
            is ExpectedDownloadItemState -> this.uri == other.uri
            is ActualDownloadItemState   -> this.uri == other.uri
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return uri.hashCode()
    }
}

internal data class ExpectedDownloadItemState(val uri: Uri) {
    override fun equals(other: Any?): Boolean {
        return when(other) {
            is ExpectedDownloadItemState -> this.uri == other.uri
            is ActualDownloadItemState   -> this.uri == other.uri
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

        return when(other) {
            is ActualDownloadState ->  {
                this.downloads == other.downloads && this.paused == other.paused
            }
            is ExpectedDownloadState -> {
                this.downloads == other.downloads && this.paused == other.paused
            }
            else -> false
        }
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

        return when(other) {
            is ActualDownloadState ->  {
                this.downloads == other.downloads && this.paused == other.paused
            }
            is ExpectedDownloadState -> {
                this.downloads == other.downloads && this.paused == other.paused
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        var result = downloads.hashCode()
        result = 31 * result + paused.hashCode()
        return result
    }
}