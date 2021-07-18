package com.muddassir.faudio

val start: ((AudioState) -> AudioState) = {
    AudioState(
        it.uris,
        it.index,
        false,
        it.progress,
        it.speed,
        it.bufferedPosition,
        it.currentIndexDuration,
        false,
        it.error
    )
}

val pause: ((AudioState) -> AudioState) = {
    AudioState(
        it.uris,
        it.index,
        true,
        it.progress,
        it.speed,
        it.bufferedPosition,
        it.currentIndexDuration,
        false,
        it.error
    )
}

val stop: ((AudioState) -> AudioState) = {
    AudioState(
        it.uris,
        it.index,
        true,
        0,
        it.speed,
        it.bufferedPosition,
        it.currentIndexDuration,
        true,
        it.error
    )
}

val next: ((AudioState) -> AudioState) = {
    val nextIndex = (it.index+1)%it.uris.size

    AudioState(
        it.uris,
        nextIndex,
        false,
        0,
        it.speed,
        it.bufferedPosition,
        it.currentIndexDuration,
        false,
        it.error
    )
}

val prev: ((AudioState) -> AudioState) = {
    val prevIndex = (it.index-1)%it.uris.size

    AudioState(
        it.uris,
        prevIndex,
        false,
        0,
        it.speed,
        it.bufferedPosition,
        it.currentIndexDuration,
        false,
        it.error
    )
}

val seekTo = { currentState: AudioState, millis: Long ->
    AudioState(
        currentState.uris,
        currentState.index,
        false,
        millis,
        currentState.speed,
        currentState.bufferedPosition,
        currentState.currentIndexDuration,
        false,
        currentState.error
    )
}

val restart: ((AudioState) -> AudioState) = {
    AudioState(
        it.uris,
        it.index,
        false,
        0,
        it.speed,
        it.bufferedPosition,
        it.currentIndexDuration,
        false,
        it.error
    )
}

val shuffle: ((AudioState) -> AudioState) = {
    val randIndex = it.uris.indices.shuffled().last()

    AudioState(
        it.uris,
        randIndex,
        false,
        0,
        it.speed,
        it.bufferedPosition,
        it.currentIndexDuration,
        false,
        it.error
    )
}