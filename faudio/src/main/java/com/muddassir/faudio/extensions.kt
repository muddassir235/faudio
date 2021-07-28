package com.muddassir.faudio

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.android.exoplayer2.Player

internal val AudioProducer.uris         : List<Uri> get() = (0 until this.mediaItemCount).map { this.getMediaItemAt(it).playbackProperties?.uri!! }
internal val AudioProducer.currentIndex : Int       get() = this.currentWindowIndex
internal val AudioProducer.started      : Boolean   get() = this.playbackState == Player.STATE_READY && this.playWhenReady
internal val AudioProducer.buffering    : Boolean   get() = this.playbackState == Player.STATE_BUFFERING && this.playWhenReady
internal val AudioProducer.speed        : Float     get() = this.playbackParameters.speed
internal val AudioProducer.stopped      : Boolean   get() = this.playbackState == Player.STATE_IDLE || this.playbackState == Player.STATE_ENDED
internal val AudioProducer.error        : String?   get() = this.playerError?.localizedMessage

internal fun AudioProducer.resume() { if(!stopped) this.start() }
internal fun AudioProducer.startCheckFocus(focused: Boolean) { if(focused) this.start() }
internal fun AudioProducer.start() { this.play() }
internal fun AudioProducer.setUris(uris: List<Uri>) {
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

infix fun ((ActualAudioState) -> ExpectedAudioState).then(
    other: ((ActualAudioState) -> ExpectedAudioState)): ((ActualAudioState) -> ExpectedAudioState) = {
    other(expectedToActualState(this(it)))
}

infix fun Iterable<String>.asAudioWith(context: Context): Audio {
    return Audio(context, uris = this.map { Uri.parse(it) })
}