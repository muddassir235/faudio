package com.muddassir.faudio

import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource

typealias MediaSource          = ConcatenatingMediaSource
typealias AudioProducer        = SimpleExoPlayer
typealias AudioProducerBuilder = SimpleExoPlayer.Builder
typealias AudioObserver        = ((audioObservation : AudioObservation) -> Unit)