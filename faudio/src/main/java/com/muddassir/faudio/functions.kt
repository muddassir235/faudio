package com.muddassir.faudio

val start: ((ActualAudioState) -> ExpectedAudioState) = {
    ExpectedAudioState(
        audios = it.audios.map(actualAudioItemToExpectedAudioItem),
        index = it.index,
        paused = false,
        progress = it.progress,
        speed = it.speed,
        stopped = false
    )
}

val startAndDownload: ((ActualAudioState) -> ExpectedAudioState) = {
    ExpectedAudioState(
        audios = it.audios.mapIndexed{ index, audioItem ->
            ExpectedAudioItem(audioItem.uri, audioItem.download || index == it.index)
        },
        index = it.index,
        paused = false,
        progress = it.progress,
        speed = it.speed,
        stopped = false
    )
}

val downloadCurrent: ((ActualAudioState) -> ExpectedAudioState) = {
    ExpectedAudioState(
        audios = it.audios.mapIndexed{ index, audioItem ->
            ExpectedAudioItem(audioItem.uri, audioItem.download || index == it.index)
        },
        index = it.index,
        paused = it.paused,
        progress = it.progress,
        speed = it.speed,
        stopped = it.stopped
    )
}

val pause: ((ActualAudioState) -> ExpectedAudioState) = {
    ExpectedAudioState(
        audios = it.audios.map(actualAudioItemToExpectedAudioItem),
        index = it.index,
        paused = true,
        progress = it.progress,
        speed = it.speed,
        stopped = false
    )
}

val stop: ((ActualAudioState) -> ExpectedAudioState) = {
    ExpectedAudioState(
        audios = it.audios.map(actualAudioItemToExpectedAudioItem),
        index = it.index,
        paused = true,
        progress = 0,
        speed = it.speed,
        stopped = true
    )
}

val next: ((ActualAudioState) -> ExpectedAudioState) = {
    val nextIndex = (it.index+1)%it.audios.size

    ExpectedAudioState(
        audios = it.audios.map(actualAudioItemToExpectedAudioItem),
        index = nextIndex,
        paused = false,
        progress = 0,
        speed = it.speed,
        stopped = false
    )
}

val nextAndDownload: ((ActualAudioState) -> ExpectedAudioState) = {
    val nextIndex = (it.index+1)%it.audios.size

    ExpectedAudioState(
        audios = it.audios.mapIndexed{ index, audioItem ->
            ExpectedAudioItem(audioItem.uri, audioItem.download || index == nextIndex)
        },
        index = nextIndex,
        paused = false,
        progress = 0,
        speed = it.speed,
        stopped = false
    )
}


val prev: ((ActualAudioState) -> ExpectedAudioState) = {
    val prevIndex = (it.index-1)%it.audios.size

    ExpectedAudioState(
        audios = it.audios.map(actualAudioItemToExpectedAudioItem),
        index = prevIndex,
        paused = false,
        progress = 0,
        speed = it.speed,
        stopped = false
    )
}

val prevAndDownload: ((ActualAudioState) -> ExpectedAudioState) = {
    val prevIndex = (it.index-1)%it.audios.size

    ExpectedAudioState(
        audios = it.audios.mapIndexed{ index, audioItem ->
            ExpectedAudioItem(audioItem.uri, audioItem.download || index == prevIndex)
        },
        index = prevIndex,
        paused = false,
        progress = 0,
        speed = it.speed,
        stopped = false
    )
}

val moveToIndex = { currentState: ActualAudioState, index: Int ->
    ExpectedAudioState(
        audios = currentState.audios.map(actualAudioItemToExpectedAudioItem),
        index = index,
        paused = false,
        progress = 0,
        speed = currentState.speed,
        stopped = false
    )
}

val moveToIndexAndDownload = { currentState: ActualAudioState, index: Int ->
    ExpectedAudioState(
        audios = currentState.audios.mapIndexed{ mapIndex, audioItem ->
            ExpectedAudioItem(audioItem.uri, audioItem.download || mapIndex == index)
        },
        index = index,
        paused = false,
        progress = 0,
        speed = currentState.speed,
        stopped = false
    )
}

val downloadIndex = { currentState: ActualAudioState, index: Int ->
    ExpectedAudioState(
        audios = currentState.audios.mapIndexed{ mapIndex, audioItem ->
            ExpectedAudioItem(audioItem.uri, audioItem.download || mapIndex == index)
        },
        index = currentState.index,
        paused = currentState.paused,
        progress = currentState.progress,
        speed = currentState.speed,
        stopped = currentState.stopped
    )
}

val seekTo = { currentState: ActualAudioState, millis: Long ->
    ExpectedAudioState(
        audios = currentState.audios.map(actualAudioItemToExpectedAudioItem),
        index = currentState.index,
        paused = false,
        progress = millis,
        speed = currentState.speed,
        stopped = false
    )
}

val restart: ((ActualAudioState) -> ExpectedAudioState) = {
    ExpectedAudioState(
        audios = it.audios.map(actualAudioItemToExpectedAudioItem),
        index = it.index,
        paused = false,
        progress = 0,
        speed = it.speed,
        stopped = false
    )
}

val shuffle: ((ActualAudioState) -> ExpectedAudioState) = {
    val randIndex = it.audios.indices.shuffled().last()

    ExpectedAudioState(
        audios = it.audios.map(actualAudioItemToExpectedAudioItem),
        index = randIndex,
        paused = false,
        progress = 0,
        speed = it.speed,
        stopped = false
    )
}