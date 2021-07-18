package com.muddassir.faudio

import android.net.Uri
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers.anyString
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.mock
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(PowerMockRunner::class)
@PrepareForTest(Uri::class)
@PowerMockIgnore("jdk.internal.reflect.*")
class TestAudioState {
    private lateinit var audioState: ActualState
    
    @Before
    fun setup() {
        PowerMockito.mockStatic(Uri::class.java)
        val uri: Uri = mock(Uri::class.java)

        PowerMockito.`when`<Any>(Uri::class.java, "parse", anyString()).thenReturn(uri)

        val uris = arrayOf(
            Uri.parse("https://audio-samples.github.io/samples/mp3/blizzard_unconditional/sample-5.mp3"),
            Uri.parse("https://audio-samples.github.io/samples/mp3/blizzard_primed/sample-0.mp3"),
            Uri.parse("https://audio-samples.github.io/samples/mp3/blizzard_unconditional/sample-0.mp3")
        )

        audioState = ActualState(
            uris = uris,
            index =0,
            paused = true,
            progress =0L,
            speed = 1.0f,
            bufferedPosition = 0L,
            currentIndexDuration = 0L,
            stopped = true,
            error = null
        )
    }

    @Test
    fun testStart() {
        val started = start(audioState)
        assertTrue(started == ExpectedState(
            audioState.uris,
            audioState.index,
            false,
            audioState.progress,
            audioState.speed,
            false
        )
        )
    }

    @Test
    fun testPause() {
        val modifiedActualState = ActualState(
            audioState.uris,
            audioState.index,
            false,
            audioState.progress,
            audioState.speed,
            audioState.bufferedPosition,
            audioState.currentIndexDuration,
            false,
            audioState.error
        )

        val paused = pause(modifiedActualState)
        assertTrue(paused == ExpectedState(
            modifiedActualState.uris,
            modifiedActualState.index,
            true,
            modifiedActualState.progress,
            modifiedActualState.speed,
            false
        ))
    }

    @Test
    fun testStop() {
        val modifiedActualState = ActualState(
            audioState.uris,
            audioState.index,
            false,
            audioState.progress,
            audioState.speed,
            audioState.bufferedPosition,
            audioState.currentIndexDuration,
            false,
            audioState.error
        )

        val stopped = stop(modifiedActualState)
        assertTrue(stopped == ExpectedState(
            modifiedActualState.uris,
            modifiedActualState.index,
            true,
            modifiedActualState.progress,
            modifiedActualState.speed,
            true
        ))
    }

    @Test
    fun testNext() {
        val nextIndex = (audioState.index+1)%audioState.uris.size

        val next = next(audioState)
        assertTrue(next == ExpectedState(
            audioState.uris,
            nextIndex,
            false,
            0,
            audioState.speed,
            false
        ))
    }

    @Test
    fun testPrev() {
        val modifiedActualState = ActualState(
            audioState.uris,
            3,
            audioState.paused,
            audioState.progress,
            audioState.speed,
            audioState.bufferedPosition,
            audioState.currentIndexDuration,
            audioState.stopped,
            audioState.error
        )

        val prevIndex = (modifiedActualState.index-1)%modifiedActualState.uris.size

        val prev = prev(modifiedActualState)
        assertTrue(prev == ExpectedState(
            modifiedActualState.uris,
            prevIndex,
            false,
            0,
            modifiedActualState.speed,
            false
        ))
    }

    @Test
    fun testSeekTo() {
        val seeked = seekTo(audioState, 100000)
        assertTrue(seeked == ExpectedState(
            audioState.uris,
            audioState.index,
            false,
            100000,
            audioState.speed,
            false
        ))
    }

    @Test
    fun testMoveToIndex() {
        val moved = moveToIndex(audioState, 3)
        assertTrue(moved == ExpectedState(
            audioState.uris,
            3,
            false,
            0,
            audioState.speed,
            false
        ))
    }

    @Test
    fun testRestart() {
        val modifiedActualState = ActualState(
            audioState.uris,
            audioState.index,
            audioState.paused,
            10000L,
            audioState.speed,
            audioState.bufferedPosition,
            audioState.currentIndexDuration,
            audioState.stopped,
            audioState.error
        )

        val restarted = restart(modifiedActualState)
        assertTrue(restarted == ExpectedState(
            modifiedActualState.uris,
            modifiedActualState.index,
            false,
            0,
            modifiedActualState.speed,
            false
        ))
    }

    @Test
    fun testShuffle() {
        val shuffled = shuffle(audioState)

        assertTrue(shuffled.uris.contentEquals(audioState.uris))
        assertEquals(shuffled.paused, false)
        assertEquals(shuffled.progress, 0)
        assertEquals(shuffled.speed, audioState.speed)
        assertEquals(shuffled.stopped, false)
    }
}