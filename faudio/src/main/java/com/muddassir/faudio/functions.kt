package com.muddassir.faudio

import android.net.Uri
import com.google.android.exoplayer2.MediaItem

val start: ((ActualState) -> ExpectedState) = {
    ExpectedState(
        uris = it.uris,
        index = it.index,
        paused = false,
        progress = it.progress,
        speed = it.speed,
        stopped = false
    )
}

val pause: ((ActualState) -> ExpectedState) = {
    ExpectedState(
        uris = it.uris,
        index = it.index,
        paused = true,
        progress = it.progress,
        speed = it.speed,
        stopped = false
    )
}

val stop: ((ActualState) -> ExpectedState) = {
    ExpectedState(
        uris = it.uris,
        index = it.index,
        paused = true,
        progress = 0,
        speed = it.speed,
        stopped = true
    )
}

val next: ((ActualState) -> ExpectedState) = {
    val nextIndex = (it.index+1)%it.uris.size

    ExpectedState(
        uris = it.uris,
        index = nextIndex,
        paused = false,
        progress = 0,
        speed = it.speed,
        stopped = false
    )
}

val prev: ((ActualState) -> ExpectedState) = {
    val prevIndex = (it.index-1)%it.uris.size

    ExpectedState(
        uris = it.uris,
        index = prevIndex,
        paused = false,
        progress = 0,
        speed = it.speed,
        stopped = false
    )
}

val moveToIndex = { currentState: ActualState, index: Int ->
    ExpectedState(
        uris = currentState.uris,
        index = index,
        paused = false,
        progress = 0,
        speed = currentState.speed,
        stopped = false
    )
}


val seekTo = { currentState: ActualState, millis: Long ->
    ExpectedState(
        uris = currentState.uris,
        index = currentState.index,
        paused = false,
        progress = millis,
        speed = currentState.speed,
        stopped = false
    )
}

val restart: ((ActualState) -> ExpectedState) = {
    ExpectedState(
        uris = it.uris,
        index = it.index,
        paused = false,
        progress = 0,
        speed = it.speed,
        stopped = false
    )
}

val shuffle: ((ActualState) -> ExpectedState) = {
    val randIndex = it.uris.indices.shuffled().last()

    ExpectedState(
        uris = it.uris,
        index = randIndex,
        paused = false,
        progress = 0,
        speed = it.speed,
        stopped = false
    )
}

internal val uriToMediaItem: ((Uri) -> MediaItem) = { MediaItem.fromUri(it) }