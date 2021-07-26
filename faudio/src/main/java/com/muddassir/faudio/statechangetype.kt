package com.muddassir.faudio

class AudioStateChangeTypes {
    companion object {
        const val START = "start"
        const val START_AND_DOWNLOAD = "startAndDownload"
        const val DOWNLOAD_CURRENT = "downloadCurrent"
        const val PAUSE = "pause"
        const val STOP  = "stop"
        const val NEXT  = "next"
        const val NEXT_AND_DOWNLOAD  = "nextAndDownload"
        const val PREV  = "prev"
        const val PREV_AND_DOWNLOAD  = "prevAndDownload"
        const val SEEK  = "seek"
        const val MOVE_TO_INDEX = "moveToIndex"
        const val MOVE_TO_INDEX_AND_DOWNLOAD = "moveToIndexAndDownload"
        const val DOWNLOAD_INDEX = "downloadIndex"
        const val RESTART = "restart"
        const val URIS_CHANGED = "uris_changed"
        const val UNCHANGED = "unchanged"
        const val UNKNOWN = "unknown"
    }
}

data class AudioStateDiff(
    val prev: ActualAudioState?,
    val next: ActualAudioState,
    val changeType: String?
)

fun ActualAudioState.changeType(other: ActualAudioState): String {
    return if(this.audios != other.audios)
        AudioStateChangeTypes.URIS_CHANGED
    else if(this.stopped != other.stopped && other.stopped)
        AudioStateChangeTypes.STOP
    else if(this.paused != other.paused && other.paused)
        AudioStateChangeTypes.PAUSE
    else if(this.index != other.index && other.index == this.index+1
        && other.audios[other.index].download && !this.audios[other.index].download)
        AudioStateChangeTypes.NEXT_AND_DOWNLOAD
    else if(this.index != other.index && other.index == this.index+1)
        AudioStateChangeTypes.NEXT
    else if(this.index != other.index && other.index == this.index-1
        && other.audios[other.index].download && !this.audios[other.index].download)
        AudioStateChangeTypes.PREV_AND_DOWNLOAD
    else if(this.index != other.index && other.index == this.index-1)
        AudioStateChangeTypes.PREV
    else if(this.index != other.index && !this.audios[other.index].download
        && other.audios[other.index].download)
        AudioStateChangeTypes.MOVE_TO_INDEX_AND_DOWNLOAD
    else if(this.index != other.index)
        AudioStateChangeTypes.MOVE_TO_INDEX
    else if(this.paused != other.paused && !other.paused
        && !this.audios[this.index].download
        && other.audios[other.index].download)
        AudioStateChangeTypes.START_AND_DOWNLOAD
    else if(this.paused != other.paused && !other.paused)
        AudioStateChangeTypes.START
    else if((this.progress > 0 || this.bufferedPosition > 0) && other.progress == 0L
        && other.index == this.index)
        AudioStateChangeTypes.RESTART
    else if(this.progress != other.progress)
        AudioStateChangeTypes.SEEK
    else if(this.paused == other.paused && this.stopped == other.stopped &&
        this.index == other.index && this.progress == other.progress && this.audios == other.audios
        && !this.audios[this.index].download && other.audios[other.index].download)
        AudioStateChangeTypes.DOWNLOAD_CURRENT
    else if(this.paused == other.paused && this.stopped == other.stopped &&
        this.index == other.index && this.progress == other.progress
        && this.audios == other.audios &&
        other.audios.count { it.download } == this.audios.count { it.download } + 1)
        AudioStateChangeTypes.DOWNLOAD_INDEX
    else if(this.paused == other.paused && this.stopped == other.stopped &&
        this.index == other.index && this.progress == other.progress
        && this.audios == other.audios)
        AudioStateChangeTypes.UNCHANGED
    else AudioStateChangeTypes.UNKNOWN
}