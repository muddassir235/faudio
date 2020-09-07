# Faudio
[![Release](https://jitpack.io/v/muddassir235/faudio.svg?style=flat-square)](https://jitpack.io/#muddassir235/faudio/)

Android Audio Library, leaning towards a functional programming style, written in Kotlin. Written on top of [ExoPlayer](https://github.com/google/ExoPlayer).

## Unique Features
* A simple API is provided which leans towards a functional programming style.
* A range of arbitray custom functions can be written to perform actions on the audio.
* Hides away all of the video related features of ExoPlayer so you can focus on your audio application.

## Add Dependencies

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
    implementation 'com.github.muddassir235:faudio:1.4'
}
```

## Use The Library
Create an audio pill with a starting state
```kotlin
val audioPill = AudioPill(Audio(
    prevAudio = null,
    uris = uris(
        "https://site.com/track1.mp3", 
        "https://site.com/track2.mp3", 
        "https://site.com/track3.mp3"
    ),
    context = this,
    audioState = AudioStateInput(
        paused = false,
        index = 0,
        progress = 0,
        stopped = false
    )
))
```

Perform any common action on your audio
```kotlin
audioPill.act(start)
audioPill.act(pause)
audioPill.act(stop)
audioPill.act(next)
audioPill.act(prev)
audioPill.act(restart)
audioPill.act(shuffle)
audioPill.act {
    seekTo(it, 60000)
}
audioPill.act {
    addObserver(it) {
        // it.error
        // it.stopped
        // it.paused
        // it.index
        // it.progress
        // it.bufferedPosition
        // it.duration
    }
}
```
Perform a custom action on your audio using a lambda
```kotlin
audioPill.act{ current ->
  // Perform a custom action based on the current state of the audio.
  // e.g. This action flips the paused state of the audio (so if its paused it gets started, if its start it gets paused)
  // Any similar custom action can be performed
  
  Audio(
    prevAudio = current,
    uris = current.uris,
    context = current.context,
    audioState = AudioStateInput(
      paused = !current.audioState.paused,
      index = 0,
      progress = 0,
      stopped = false
   )
 )
}
```

## Credits:
This library used the following projects.

* https://github.com/google/ExoPlayer
* https://github.com/muddassir235/kmacros
