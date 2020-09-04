package com.muddassir.faudio

val start: ((Audio) -> Audio) = {
    Audio(it, it.context, it.uris, AudioStateInput(
        it.audioState.index,
        false,
        it.audioState.progress,
        false
    ))
}

val pause: ((Audio) -> Audio) = {
    Audio(it, it.context, it.uris, AudioStateInput(
        it.audioState.index,
        true,
        it.audioState.progress,
        false
    ))
}

val stop: ((Audio) -> Audio) = {
    Audio(it, it.context, it.uris, AudioStateInput(
        it.audioState.index,
        true,
        it.audioState.progress,
        true
    ))
}

val next: ((Audio) -> Audio) = {
    val nextIndex = (it.audioState.index+1)%it.uris.size

    Audio(it, it.context, it.uris, AudioStateInput(
        nextIndex,
        false,
        0,
        false
    ))
}

val prev: ((Audio) -> Audio) = {
    val prevIndex = (it.audioState.index-1)%it.uris.size

    Audio(it, it.context, it.uris, AudioStateInput(
        prevIndex,
        false,
        0,
        false
    ))
}

val seekTo = { audio: Audio, millis: Long ->
    Audio(audio, audio.context, audio.uris, AudioStateInput(
        audio.audioState.index,
        false,
        millis,
        false
    ))
}

val restart: ((Audio) -> Audio) = {
    val prevIndex = (it.audioState.index-1)%it.uris.size

    Audio(it, it.context, it.uris, AudioStateInput(
        prevIndex,
        false,
        0,
        false
    ))
}

val shuffle: ((Audio) -> Audio) = {
    val randIndex = it.uris.indices.shuffled().last()

    Audio(it, it.context, it.uris, AudioStateInput(
        randIndex,
        false,
        0,
        false
    ))
}