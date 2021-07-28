# Faudio
[![Release](https://jitpack.io/v/muddassir235/faudio.svg?style=flat-square)](https://jitpack.io/#muddassir235/faudio/)

Android Audio Library, leaning towards a functional programming style, written in Kotlin. Written on top of [ExoPlayer](https://github.com/google/ExoPlayer).

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
val audio = listOf(
    "https://site.com/audio1.mp3",
    "https://site.com/audio2.mp3",
    "https://site.com/audio3.mp3"
) asAudioWith(this)

audio should (start then download)
```

### Available audio actions
Perform any common action on your audio

On the main thread
```kotlin
audio should start
audio should pause
audio should stop
audio should moveToNext
audio should moveToPrev
.
.
.

// If status of the action (success or failure) is required use the following
audio.changeStateAsync(start) { success ->
    // Check the operation completion status if required.
}
```

Using coroutines
```kotlin
audio shouldPerform {
    // suspend methods
    this needsTo download
    this needsTo start
    this needsTo shuffle

    val success = this needsTo moveToNext // Check status if required
    .
    .
    .
}
```

### Custom actions

Perform a custom action on your audio using a lambda

Main thread
```kotlin
audio should { actualState ->
    // Your own custom action of the audio
    ExpectedAudioState(
        uris = actualState.uris,
        index = /* new index */,
        paused = /* should it be paused */,
        progress = /* new progress */,
        speed = /* updated speed */,
        stopped = /* should it stop */
    )
}
```

Coroutines
```kotlin
audio shouldPerform {
    this needsTo {
        // Your own custom action of the audio
        ExpectedAudioState(
            uris = actualState.uris,
            index = /* new index */,
            paused = /* should it be paused */,
            progress = /* new progress */,
            speed = /* updated speed */,
            stopped = /* should it stop */
        )
    }
}
```
### Observe state
Observe the audio state or state diffs on every change of state.

State changes
```kotlin
audio.state.observe(lifecycleScope) { actualState ->
    // Your logic here
    // Fields available...
    // actualState.uris, actualState.index, actualState.paused, actualState.progress
    // actualState.speed, actualState.bufferedPosition, actualState.currentIndexDuration,
    // actualState.stopped, actualState.error
}
```

State diffs
```kotlin
audio.stateDiff.observe(lifecycleScope) { diff ->
    // Your logic here
    // Fields available...
    // diff.prev Previous state
    // diff.next Next state
    // diff.changeType (Can be one of the those defined in AudioStateChangeTypes)
}
```
The following are the available state change types
```kotlin
AudioStateChangeTypes.START
AudioStateChangeTypes.PAUSE
AudioStateChangeTypes.STOP
AudioStateChangeTypes.NEXT
AudioStateChangeTypes.PREV
AudioStateChangeTypes.SEEK
AudioStateChangeTypes.MOVE_TO_INDEX
AudioStateChangeTypes.RESTART
AudioStateChangeTypes.URIS_CHANGED
AudioStateChangeTypes.UNCHANGED
AudioStateChangeTypes.UNKNOWN
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
