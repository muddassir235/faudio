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
internal val AudioProducer.audioState   : ActualState get() = ActualState(this.uris, this.currentIndex, !this.started, this.currentPosition, this.speed,
    this.bufferedPosition, this.duration, this.stopped, this.error)

internal fun AudioProducer.resume() { if(!stopped) this.play() }
internal fun AudioProducer.startCheckFocus(focused: Boolean) { if(focused) this.play() }
internal fun AudioProducer.start() { this.play() }
internal fun AudioProducer.setUris(uris: Array<Uri>) {
    this.setMediaItems(uris.map(uriToMediaItem))
    this.prepare()
}
internal fun AudioProducer.setAudioSpeed(speed: Float) { this.setPlaybackSpeed(speed) }

val Audio.audioStateDiff: LiveData<AudioStateDiff> get() {
    var prev: ActualState? = null
    val diffLd = MediatorLiveData<AudioStateDiff>()

    diffLd.addSource(audioState) {
        diffLd.value = AudioStateDiff(prev, it, prev?.audioStateChange(it))

        prev = it
    }

    return diffLd
}

class AudioStateChangeKeys {
    companion object {
        val START = "start"
        val PAUSE = "pause"
        val STOP  = "stop"
        val NEXT  = "next"
        val PREV  = "prev"
        val SEEK  = "seek"
        val MOVE_TO_INDEX = "change_position"
        val RESTART = "restart"
        const val URIS_CHANGED = "uris_changed"
        val UNCHANGED = "unchanged"
        val UNKNOWN = "unknown"
    }
}

data class AudioStateDiff(
    val prev: ActualState?,
    val next: ActualState,
    val audioStateChangeKey: String?
)

fun ActualState.audioStateChange(next: ActualState): String {
    return if(!this.uris.contentEquals(next.uris))
        AudioStateChangeKeys.URIS_CHANGED
    else if(this.stopped != next.stopped && next.stopped)
        AudioStateChangeKeys.STOP
    else if(this.paused != next.paused && next.paused)
        AudioStateChangeKeys.PAUSE
    else if(this.index != next.index && next.index == this.index+1)
        AudioStateChangeKeys.NEXT
    else if(this.index != next.index && next.index == this.index-1)
        AudioStateChangeKeys.PREV
    else if(this.index != next.index)
        AudioStateChangeKeys.MOVE_TO_INDEX
    else if(this.paused != next.paused && !next.paused)
        AudioStateChangeKeys.START
    else if((this.progress > 0 || this.bufferedPosition > 0) && next.progress == 0L
        && next.index == this.index)
        AudioStateChangeKeys.RESTART
    else if(this.progress != next.progress)
        AudioStateChangeKeys.SEEK
    else if(this.paused == next.paused && this.stopped == next.stopped &&
        this.index == next.index && this.progress == next.progress
        && this.uris.contentEquals(next.uris))
        AudioStateChangeKeys.UNCHANGED
    else AudioStateChangeKeys.UNKNOWN
}