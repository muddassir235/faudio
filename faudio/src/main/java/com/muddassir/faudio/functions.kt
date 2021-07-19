package com.muddassir.faudio

import android.net.Uri
import com.google.android.exoplayer2.MediaItem

val start: ((ActualAudioState) -> ExpectedAudioState) = {
    ExpectedAudioState(
        uris = it.uris,
        index = it.index,
        paused = false,
        progress = it.progress,
        speed = it.speed,
        stopped = false
    )
}

val pause: ((ActualAudioState) -> ExpectedAudioState) = {
    ExpectedAudioState(
        uris = it.uris,
        index = it.index,
        paused = true,
        progress = it.progress,
        speed = it.speed,
        stopped = false
    )
}

val stop: ((ActualAudioState) -> ExpectedAudioState) = {
    ExpectedAudioState(
        uris = it.uris,
        index = it.index,
        paused = true,
        progress = 0,
        speed = it.speed,
        stopped = true
    )
}

val next: ((ActualAudioState) -> ExpectedAudioState) = {
    val nextIndex = (it.index+1)%it.uris.size

    ExpectedAudioState(
        uris = it.uris,
        index = nextIndex,
        paused = false,
        progress = 0,
        speed = it.speed,
        stopped = false
    )
}

val prev: ((ActualAudioState) -> ExpectedAudioState) = {
    val prevIndex = (it.index-1)%it.uris.size

    ExpectedAudioState(
        uris = it.uris,
        index = prevIndex,
        paused = false,
        progress = 0,
        speed = it.speed,
        stopped = false
    )
}

val moveToIndex = { currentState: ActualAudioState, index: Int ->
    ExpectedAudioState(
        uris = currentState.uris,
        index = index,
        paused = false,
        progress = 0,
        speed = currentState.speed,
        stopped = false
    )
}


val seekTo = { currentState: ActualAudioState, millis: Long ->
    ExpectedAudioState(
        uris = currentState.uris,
        index = currentState.index,
        paused = false,
        progress = millis,
        speed = currentState.speed,
        stopped = false
    )
}

val restart: ((ActualAudioState) -> ExpectedAudioState) = {
    ExpectedAudioState(
        uris = it.uris,
        index = it.index,
        paused = false,
        progress = 0,
        speed = it.speed,
        stopped = false
    )
}

val shuffle: ((ActualAudioState) -> ExpectedAudioState) = {
    val randIndex = it.uris.indices.shuffled().last()

    ExpectedAudioState(
        uris = it.uris,
        index = randIndex,
        paused = false,
        progress = 0,
        speed = it.speed,
        stopped = false
    )
}

internal val uriToMediaItem: ((Uri) -> MediaItem) = { MediaItem.fromUri(it) }