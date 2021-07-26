package com.muddassir.faudio

class AudioStateChangeTypes {
    companion object {
        const val START = "start"
        const val PAUSE = "pause"
        const val STOP  = "stop"
        const val NEXT  = "next"
        const val PREV  = "prev"
        const val SEEK  = "seek"
        const val MOVE_TO_INDEX = "change_position"
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

fun ActualAudioState.changeType(next: ActualAudioState): String {
    return if(this.audios != next.audios)
        AudioStateChangeTypes.URIS_CHANGED
    else if(this.stopped != next.stopped && next.stopped)
        AudioStateChangeTypes.STOP
    else if(this.paused != next.paused && next.paused)
        AudioStateChangeTypes.PAUSE
    else if(this.index != next.index && next.index == this.index+1)
        AudioStateChangeTypes.NEXT
    else if(this.index != next.index && next.index == this.index-1)
        AudioStateChangeTypes.PREV
    else if(this.index != next.index)
        AudioStateChangeTypes.MOVE_TO_INDEX
    else if(this.paused != next.paused && !next.paused)
        AudioStateChangeTypes.START
    else if((this.progress > 0 || this.bufferedPosition > 0) && next.progress == 0L
        && next.index == this.index)
        AudioStateChangeTypes.RESTART
    else if(this.progress != next.progress)
        AudioStateChangeTypes.SEEK
    else if(this.paused == next.paused && this.stopped == next.stopped &&
        this.index == next.index && this.progress == next.progress
        && this.audios == next.audios)
        AudioStateChangeTypes.UNCHANGED
    else AudioStateChangeTypes.UNKNOWN
}