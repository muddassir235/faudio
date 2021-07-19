package com.muddassir.faudio

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.android.exoplayer2.Player

internal val AudioProducer.uris         : Array<Uri>  get() = (0 until this.mediaItemCount).map { this.getMediaItemAt(it).playbackProperties?.uri!! }.toTypedArray()
internal val AudioProducer.currentIndex : Int         get() = this.currentWindowIndex
internal val AudioProducer.started      : Boolean     get() = this.playbackState == Player.STATE_READY && this.playWhenReady
internal val AudioProducer.speed        : Float       get() = this.playbackParameters.speed
internal val AudioProducer.stopped      : Boolean     get() = this.playbackState == Player.STATE_IDLE || this.playbackState == Player.STATE_ENDED
internal val AudioProducer.error        : String?     get() = this.playerError?.localizedMessage
internal val AudioProducer.audioState   : ActualAudioState get() = ActualAudioState(this.uris, this.currentIndex, !this.started, this.currentPosition, this.speed,
    this.bufferedPosition, this.duration, this.stopped, this.error)

internal fun AudioProducer.resume() { if(!stopped) this.play() }
internal fun AudioProducer.startCheckFocus(focused: Boolean) { if(focused) this.play() }
internal fun AudioProducer.start() { this.play() }
internal fun AudioProducer.setUris(uris: Array<Uri>) {
    this.setMediaItems(uris.map(uriToMediaItem))
    this.prepare()
}
internal fun AudioProducer.setAudioSpeed(speed: Float) { this.setPlaybackSpeed(speed) }

val Audio.stateDiff: LiveData<AudioStateDiff> get() {
    var prev: ActualAudioState? = null
    val diffLd = MediatorLiveData<AudioStateDiff>()

    diffLd.addSource(state) {
        diffLd.value = AudioStateDiff(prev, it, prev?.changeType(it))

        prev = it
    }

    return diffLd
}

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
    return if(!this.uris.contentEquals(next.uris))
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
        && this.uris.contentEquals(next.uris))
        AudioStateChangeTypes.UNCHANGED
    else AudioStateChangeTypes.UNKNOWN
}