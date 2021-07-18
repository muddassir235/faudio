package com.muddassir.faudio

import android.net.Uri
import com.google.android.exoplayer2.Player

internal val AudioProducer.uris         : Array<Uri> get() = (0 until this.mediaItemCount).map { this.getMediaItemAt(it).playbackProperties?.uri!! }.toTypedArray()
internal val AudioProducer.currentIndex : Int        get() = this.currentWindowIndex
internal val AudioProducer.started      : Boolean    get() = this.playbackState == Player.STATE_READY && this.playWhenReady
internal val AudioProducer.speed        : Float      get() = this.playbackParameters.speed
internal val AudioProducer.stopped      : Boolean    get() = this.playbackState == Player.STATE_IDLE || this.playbackState == Player.STATE_ENDED
internal val AudioProducer.error        : String?    get() = this.playerError?.localizedMessage
internal val AudioProducer.audioState   : AudioState get() = AudioState(this.uris, this.currentIndex, !this.started, this.currentPosition, this.speed,
    this.bufferedPosition, this.duration, this.stopped, this.error)

internal fun AudioProducer.resume() { if(!stopped) this.play() }
internal fun AudioProducer.startCheckFocus(focused: Boolean) { if(focused) this.play() }
internal fun AudioProducer.start() { this.play() }
internal fun AudioProducer.setUris(uris: Array<Uri>) {
    this.setMediaItems(uris.map(uriToMediaItem))
    this.prepare()
}