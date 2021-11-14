package com.muddassir.faudio

val start: ((ActualAudioState) -> ExpectedAudioState) = {
    ExpectedAudioState(
        items = it.items.map(actualToExpectedItem),
        index = it.index,
        paused = false,
        progress = it.progress,
        speed = it.speed,
        stopped = false
    )
}

val download: ((ActualAudioState) -> ExpectedAudioState) = {
    ExpectedAudioState(
        items = it.items.mapIndexed{ index, audioItem ->
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
        items = it.items.map(actualToExpectedItem),
        index = it.index,
        paused = true,
        progress = it.progress,
        speed = it.speed,
        stopped = false
    )
}

val stop: ((ActualAudioState) -> ExpectedAudioState) = {
    ExpectedAudioState(
        items = it.items.map(actualToExpectedItem),
        index = it.index,
        paused = true,
        progress = 0,
        speed = it.speed,
        stopped = true
    )
}

val moveToNext: ((ActualAudioState) -> ExpectedAudioState) = {
    val nextIndex = (it.index+1)%it.items.size

    ExpectedAudioState(
        items = it.items.map(actualToExpectedItem),
        index = nextIndex,
        paused = false,
        progress = 0,
        speed = it.speed,
        stopped = false
    )
}

val moveToPrev: ((ActualAudioState) -> ExpectedAudioState) = {
    val prevIndex = (it.index-1)%it.items.size

    ExpectedAudioState(
        items = it.items.map(actualToExpectedItem),
        index = prevIndex,
        paused = false,
        progress = 0,
        speed = it.speed,
        stopped = false
    )
}

val moveToIndex = { currentState: ActualAudioState, index: Int ->
    ExpectedAudioState(
        items = currentState.items.map(actualToExpectedItem),
        index = index,
        paused = false,
        progress = 0,
        speed = currentState.speed,
        stopped = false
    )
}

val downloadIndex = { currentState: ActualAudioState, index: Int ->
    ExpectedAudioState(
        items = currentState.items.mapIndexed{ mapIndex, audioItem ->
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
        items = currentState.items.map(actualToExpectedItem),
        index = currentState.index,
        paused = false,
        progress = millis,
        speed = currentState.speed,
        stopped = false
    )
}

val restart: ((ActualAudioState) -> ExpectedAudioState) = {
    ExpectedAudioState(
        items = it.items.map(actualToExpectedItem),
        index = it.index,
        paused = false,
        progress = 0,
        speed = it.speed,
        stopped = false
    )
}

val shuffle: ((ActualAudioState) -> ExpectedAudioState) = {
    val randIndex = it.items.indices.shuffled().last()

    ExpectedAudioState(
        items = it.items.map(actualToExpectedItem),
        index = randIndex,
        paused = false,
        progress = 0,
        speed = it.speed,
        stopped = false
    )
}