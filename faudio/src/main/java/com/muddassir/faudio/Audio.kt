package com.muddassir.faudio

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.muddassir.kmacros.delay
import com.muddassir.kmacros.safe

class Audio(prevAudio: Audio? = null, val context: Context, val uris: Array<Uri>,
            val audioState: AudioStateInput): Player.Listener,
    AudioManager.OnAudioFocusChangeListener {
    val observers: HashSet<AudioObserver> = HashSet()

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var inFocus = audioManager.gainFocus(this)

    private val ap: AudioProducer = prevAudio?.ap ?: AudioProducerBuilder(context).build()

    private var startWhenReady : Boolean get() = ap.playWhenReady
        set(value) {
            if(inFocus || !value) ap.playWhenReady = value
            else { this.invokeAudioObservers("Focus Gain Failed.") }
        }

    init {
        if(prevAudio == null || !prevAudio.uris.contentEquals(uris)) {
            ap.release()
            ap.setMediaSource(mediaSourceFromUrls(context, uris))
            ap.prepare()
        }

        ap.addListener(this)

        if(prevAudio?.currentIndex == audioState.index) {
            ap.seekTo(audioState.progress)
        } else {
            ap.seekTo(audioState.index, audioState.progress)
        }

        if(prevAudio == null || prevAudio.started == audioState.paused) {
            this.startWhenReady = !audioState.paused
        }
        
        if(audioState.stopped) ap.stop()

        this.invokeAudioObservers()
    }

    val currentIndex    : Int     get() = ap.currentWindowIndex
    val started         : Boolean get() = ap.playbackState == Player.STATE_READY && startWhenReady
    val currentPosition : Long    get() = ap.currentPosition
    private val duration        : Long    get() = ap.duration
    private val bufferedPosition: Long    get() = ap.bufferedPosition
    val stopped         : Boolean get() = ap.playbackState == Player.STATE_IDLE
    val error           : String? get() = ap.playbackError?.localizedMessage

    private fun invokeAudioObservers(error: String?) {
        safe { observers.forEach { o -> o.invoke(
            AudioObservation(
                error, this.stopped,
                !this.started, this.currentIndex, this.currentPosition, this.bufferedPosition,
                this.duration
            )
        ) } }
    }

    private fun invokeAudioObservers() { this.invokeAudioObservers(error) }

    override fun onIsPlayingChanged(isPlaying: Boolean)      { this.invokeAudioObservers() }
    override fun onPlayerStateChanged(pwr: Boolean, ps: Int) {
        if(started and !stopped) trackProgress()
    }
    override fun onPlayerError(error: ExoPlaybackException)  { this.invokeAudioObservers()  }
    override fun onPositionDiscontinuity(reason: Int) {
        if(reason == Player.DISCONTINUITY_REASON_AUTO_TRANSITION
            || reason == Player.DISCONTINUITY_REASON_SEEK_ADJUSTMENT
            || reason == Player.DISCONTINUITY_REASON_SEEK) this.invokeAudioObservers()
    }
    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
        if(reason == Player.TIMELINE_CHANGE_REASON_SOURCE_UPDATE
            || reason == Player.TIMELINE_CHANGE_REASON_SOURCE_UPDATE) this.invokeAudioObservers()
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if(this.stopped) return

        if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT
            || focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            this.inFocus = false
            this.startWhenReady = false
        } else if(focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            this.inFocus = true
            this.startWhenReady = true
        }
    }

    fun release() {
        this.ap.release()
        this.observers.clear()
    }

    private val progressInterval: Long get() {
        val millisToNextTick = 1000 - this.currentPosition % 1000
        return if (millisToNextTick < 200) millisToNextTick + 1000 else millisToNextTick
    }

    private fun trackProgress() {
        fun noteProgress() {
            this.invokeAudioObservers()
            if (this.started) {
                delay(progressInterval) { noteProgress() }
            }
        }

        noteProgress()
    }
}
