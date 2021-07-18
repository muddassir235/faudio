# Faudio
[![Release](https://jitpack.io/v/muddassir235/faudio.svg?style=flat-square)](https://jitpack.io/#muddassir235/faudio/)

Android Audio Library, leaning towards a functional programming style, written in Kotlin. Written on top of [ExoPlayer](https://github.com/google/ExoPlayer).

Used in https://play.google.com/store/apps/details?id=com.muddassirkhan.quran_android

## Unique Features
* A simple API is provided which leans towards a functional programming style.
* A whole host of arbitrary custom functions can be written to perform actions on the audio.
* Hides away all of the video related features of ExoPlayer so you can focus on your audio application.

## Requirements
* Android 5+
* Lifecycle Runtime
    ```groovy
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:$lastest_version'
    ```

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
    implementation 'com.github.muddassir235:faudio:2.0'
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

### Available audio actions
Perform any common action on your audio

Using coroutines
```kotlin
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
```

On the main thread
```kotlin
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
```

### Custom actions

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
### Observe state
Observe the audio state or state diffs on every change of state.

State changes
```kotlin
audio.audioState.observer(lifecycleScope) { actualState ->
    // Your logic here
    // Fields available...
    // actualState.uris, actualState.index, actualState.paused, actualState.progress
    // actualState.speed, actualState.bufferedPosition, actualState.currentIndexDuration,
    // actualState.stopped, actualState.error
}
```

State diffs
```kotlin
audio.audioStateDiff.observer(lifecycleScope) { audioStateDiff ->
    // Your logic here
    // Fields available...
    // audioStateDiff.prev Previous state
    // audioStateDiff.next Next state
    // audioStateDiff.audioStateChangeKey (Can be one of the those defined in AudioStateChangeKeys)
}
```
The following are the available state change keys
```kotlin
AudioStateChangeKeys.START
AudioStateChangeKeys.PAUSE
AudioStateChangeKeys.STOP
AudioStateChangeKeys.NEXT
AudioStateChangeKeys.PREV
AudioStateChangeKeys.SEEK
AudioStateChangeKeys.MOVE_TO_INDEX
AudioStateChangeKeys.RESTART
AudioStateChangeKeys.URIS_CHANGED
AudioStateChangeKeys.UNCHANGED
AudioStateChangeKeys.UNKNOWN
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
