# Faudio
[![Release](https://jitpack.io/v/muddassir235/faudio.svg?style=flat-square)](https://jitpack.io/#muddassir235/faudio/)

Android Audio Library, leaning towards a functional programming style, written in Kotlin. Written on top of [ExoPlayer](https://github.com/google/ExoPlayer).

Used in https://play.google.com/store/apps/details?id=com.muddassirkhan.quran_android

## Unique Features
* A simple API is provided which leans towards a functional programming style.
* A whole host of arbitrary custom functions can be written to perform actions on the audio.
* Hides away all of the video related features of ExoPlayer so you can focus on your audio application.

## Add Dependencies
Add the following in your project level build.gradle
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
and the following in your app level build.gradle
```groovy
dependencies {
    implementation 'com.github.muddassir235:faudio:1.6'
}
```

## Use The Library
Create an audio object
```kotlin
val uris = uris(
   "https://site.com/audio1.mp3",
   "https://site.com/audio2.mp3",
   "https://site.com/audio3.mp3"
)

val audio = Audio(context = this, scope = lifecycleScope)
audio.setStateAsync(ExpectedState.defaultStateWithUris(uris))
```

Perform any common action on your audio
```kotlin
// Using coroutines
lifecycleScope.launch {
    // The changeState method returns if the operation was successful.
    val success = audio.changeState(start)
    audio.changeState(pause)
    audio.changeState(stop)
    audio.changeState(next)
    audio.changeState(prev)
    audio.changeState(restart)
    audio.changeState(shuffle)
    audio.changeState {
        seekTo(it, 60000)
    }
    audio.changeState {
        moveToIndex(it, 3)
    }
}
```kotlin
// On the main thread
audio.changeStateAsync(start) { success ->
    // Check the operation completion status if required.
}
audio.changeStateAsync(pause)
audio.changeStateAsync(stop)
audio.changeStateAsync(next)
audio.changeStateAsync(prev)
audio.changeStateAsync(restart)
audio.changeStateAsync(shuffle)
audio.changeStateAsync({
    seekTo(it, 60000)
})
audio.changeStateAsync({
    moveToIndex(it, 3)
})

Perform a custom action on your audio using a lambda
```kotlin
audio.changeState{ actualState ->
  // Perform a custom action based on the current state of the audio.
  // e.g. This action flips the paused state of the audio (so if its paused it gets started, if its start it gets paused)
  // Any similar custom action can be performed
  ExpectedState(
    uris = actualState.uris,
    index = /* new index */,
    paused = /* should it be paused */,
    progress = /* new progress */,
    speed = /* updated speed */,
    stopped = /* should it stop */
  )
}
```
Observe audio state
```kotlin
audio.audioState.observer(lifecycleScope) { actualState ->
    // Your logic here
    // Fields available...
    // actualState.uris, actualState.index, actualState.paused, actualState.progress
    // actualState.speed, actualState.bufferedPosition, actualState.currentIndexDuration,
    // actualState.stopped, actualState.error
}

audio.audioStateDiff.observer(lifecycleScope) { audioStateDiff ->
    // Your logic here
    // Fields available...
    // audioStateDiff.prev Previous state
    // audioStateDiff.next Next state
    // audioStateDiff.audioStateChangeKey (Can be one of the those defined in AudioStateChangeKeys)
}

class AudioStateChangeKeys {
    companion object {
        val START = "start"
        val PAUSE = "pause"
        val STOP  = "stop"
        val NEXT  = "next"
        val PREV  = "prev"
        val SEEK  = "seek"
        val MOVE_TO_INDEX = "change_position"
        val RESTART = "restart"
        const val URIS_CHANGED = "uris_changed"
        val UNCHANGED = "unchanged"
        val UNKNOWN = "unknown"
    }
}
```

## Credits:
This library used the following projects.

* https://github.com/google/ExoPlayer
* https://github.com/muddassir235/kmacros

## [Apps by Muddassir Ahmed](https://play.google.com/store/apps/developer?id=Muddassir+Khan):
* https://play.google.com/store/apps/details?id=com.muddassirkhan.quran_android
* https://play.google.com/store/apps/details?id=com.app.kitaabattawheed

## Muddassir Ahmed Links:

* https://www.linkedin.com/in/muddassir35/
* https://muddassirahmed.medium.com/
* https://stackoverflow.com/users/5841416/muddassir-ahmed
