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

@RunWith(PowerMockRunner::class)
@PrepareForTest(Uri::class)
@PowerMockIgnore("jdk.internal.reflect.*")
class TestAudioFunctions {
    private lateinit var audioState: ActualAudioState

    @Before
    fun setup() {
        PowerMockito.mockStatic(Uri::class.java)
        val uri: Uri = mock(Uri::class.java)

        PowerMockito.`when`<Any>(Uri::class.java, "parse", anyString()).thenReturn(uri)

        val audios = audios(
            "https://audio-samples.github.io/samples/mp3/blizzard_unconditional/sample-5.mp3",
            "https://audio-samples.github.io/samples/mp3/blizzard_primed/sample-0.mp3",
            "https://audio-samples.github.io/samples/mp3/blizzard_unconditional/sample-0.mp3"
        )

        audioState = ActualAudioState(
            items = audios,
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
        val expectedState = ExpectedAudioState(
            audioState.items.map(actualToExpectedItem),
            audioState.index,
            false,
            audioState.progress,
            audioState.speed,
            false
        )

        assertTrue(started == expectedState)
        assertTrue(started.items[started.index].download
          == expectedState.items[expectedState.index].download)
    }

    @Test
    fun testStartAndDownload() {
        val started = (start then download)(audioState)
        val expectedState = ExpectedAudioState(
            audioState.items.mapIndexed{ index, audioItem ->
                ExpectedAudioItem(audioItem.uri, audioItem.download
                        || index == audioState.index)
            },
            audioState.index,
            false,
            audioState.progress,
            audioState.speed,
            false
        )

        assertTrue(started == expectedState)
        assertTrue(started.items[started.index].download)
    }

    @Test
    fun testDownloadCurrent() {
        val download = download(audioState)
        val expectedState = ExpectedAudioState(
            audioState.items.mapIndexed{ index, audioItem ->
                ExpectedAudioItem(audioItem.uri, audioItem.download
                        || index == audioState.index)
            },
            audioState.index,
            audioState.paused,
            audioState.progress,
            audioState.speed,
            audioState.stopped
        )

        assertTrue(download == expectedState)
        assertTrue(download.items[download.index].download)
        assertTrue(expectedState.items[expectedState.index].download)
    }

    @Test
    fun testPause() {
        val modifiedActualState = ActualAudioState(
            audioState.items,
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
        val expectedState = ExpectedAudioState(
            modifiedActualState.items.map(actualToExpectedItem),
            modifiedActualState.index,
            true,
            modifiedActualState.progress,
            modifiedActualState.speed,
            false
        )

        assertTrue(paused == expectedState)

        assertFalse(paused.items[paused.index].download)
        assertFalse(expectedState.items[expectedState.index].download)
    }

    @Test
    fun testStop() {
        val modifiedActualState = ActualAudioState(
            audioState.items,
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
        val expectedState = ExpectedAudioState(
            modifiedActualState.items.map(actualToExpectedItem),
            modifiedActualState.index,
            true,
            modifiedActualState.progress,
            modifiedActualState.speed,
            true
        )

        assertTrue(stopped == expectedState)
        assertFalse(stopped.items[stopped.index].download)
        assertFalse(expectedState.items[expectedState.index].download)
    }

    @Test
    fun testNext() {
        val nextIndex = (audioState.index+1)%audioState.items.size

        val next = moveToNext(audioState)
        val expectedState = ExpectedAudioState(
            audioState.items.map(actualToExpectedItem),
            nextIndex,
            false,
            0,
            audioState.speed,
            false
        )

        assertTrue(next == expectedState)
        assertFalse(next.items[next.index].download)
        assertFalse(expectedState.items[expectedState.index].download)
    }

    @Test
    fun testNextAndDownload() {
        val nextIndex = (audioState.index+1)%audioState.items.size

        val next = (moveToNext then download)(audioState)
        val expectedState = ExpectedAudioState(
            audioState.items.mapIndexed{ index, audioItem ->
                ExpectedAudioItem(audioItem.uri, audioItem.download || index == nextIndex)
            },
            nextIndex,
            false,
            0,
            audioState.speed,
            false
        )

        assertTrue(next == expectedState)
        assertTrue(next.items[next.index].download)
        assertTrue(expectedState.items[expectedState.index].download)
    }

    @Test
    fun testPrev() {
        val modifiedActualState = ActualAudioState(
            audioState.items,
            3,
            audioState.paused,
            audioState.progress,
            audioState.speed,
            audioState.bufferedPosition,
            audioState.currentIndexDuration,
            audioState.stopped,
            audioState.error
        )

        val prevIndex = (modifiedActualState.index-1)%modifiedActualState.items.size

        val prev = moveToPrev(modifiedActualState)
        val expectedState = ExpectedAudioState(
            modifiedActualState.items.map(actualToExpectedItem),
            prevIndex,
            false,
            0,
            modifiedActualState.speed,
            false
        )

        assertTrue(prev == expectedState)
        assertFalse(prev.items[prev.index].download)
        assertFalse(expectedState.items[expectedState.index].download)
    }

    @Test
    fun testPrevAndDownload() {
        val modifiedActualState = ActualAudioState(
            audioState.items,
            3,
            audioState.paused,
            audioState.progress,
            audioState.speed,
            audioState.bufferedPosition,
            audioState.currentIndexDuration,
            audioState.stopped,
            audioState.error
        )

        val prevIndex = (modifiedActualState.index-1)%modifiedActualState.items.size

        val prev = (moveToPrev then download)(modifiedActualState)
        val expectedState = ExpectedAudioState(
            modifiedActualState.items.mapIndexed{ index, audioItem ->
                ExpectedAudioItem(audioItem.uri, audioItem.download || index == prevIndex)
            },
            prevIndex,
            false,
            0,
            modifiedActualState.speed,
            false
        )

        assertTrue(prev == expectedState)
        assertTrue(prev.items[prev.index].download)
        assertTrue(expectedState.items[expectedState.index].download)
    }

    @Test
    fun testMoveToIndex() {
        val moved = moveToIndex(audioState, 2)
        val expectedState = ExpectedAudioState(
            audioState.items.map(actualToExpectedItem),
            2,
            false,
            0,
            audioState.speed,
            false
        )

        assertTrue(moved == expectedState)
        assertFalse(moved.items[moved.index].download)
        assertFalse(expectedState.items[expectedState.index].download)
    }

    @Test
    fun testMoveToIndexAndDownload() {
        val movedAndDownloading = ({ a: ActualAudioState -> moveToIndex(a, 2) }  then download)(audioState)
        val expectedState = ExpectedAudioState(
            audioState.items.mapIndexed{ mapIndex, audioItem ->
                ExpectedAudioItem(audioItem.uri, audioItem.download || mapIndex == 2)
            },
            2,
            false,
            0,
            audioState.speed,
            false
        )

        assertTrue(movedAndDownloading == expectedState)
        assertTrue(movedAndDownloading.items[movedAndDownloading.index].download)
        assertTrue(expectedState.items[expectedState.index].download)
    }

    @Test
    fun testDownloadIndex() {
        val download = downloadIndex(audioState, 2)
        val expectedState = ExpectedAudioState(
            audioState.items.mapIndexed{ mapIndex, audioItem ->
                ExpectedAudioItem(audioItem.uri, audioItem.download || mapIndex == 2)
            },
            audioState.index,
            audioState.paused,
            audioState.progress,
            audioState.speed,
            audioState.stopped
        )

        assertTrue(download == expectedState)
        assertTrue(download.items[2].download)
        assertTrue(expectedState.items[2].download)
    }

    @Test
    fun testSeekTo() {
        val seeked = seekTo.invoke(audioState, 100000)
        val expectedState = ExpectedAudioState(
            audioState.items.map(actualToExpectedItem),
            audioState.index,
            false,
            100000,
            audioState.speed,
            false
        )

        assertTrue(seeked == expectedState)
        assertFalse(seeked.items[seeked.index].download)
        assertFalse(expectedState.items[expectedState.index].download)
    }

    @Test
    fun testRestart() {
        val modifiedActualState = ActualAudioState(
            audioState.items,
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
        val expectedState = ExpectedAudioState(
            modifiedActualState.items.map(actualToExpectedItem),
            modifiedActualState.index,
            false,
            0,
            modifiedActualState.speed,
            false
        )

        assertTrue(restarted == expectedState)
        assertFalse(restarted.items[restarted.index].download)
        assertFalse(expectedState.items[expectedState.index].download)
    }

    @Test
    fun testShuffle() {
        val shuffled = shuffle(audioState)

        assertFalse(shuffled.items[shuffled.index].download)
        assertTrue(shuffled.items == audioState.items)
        assertEquals(shuffled.paused, false)
        assertEquals(shuffled.progress, 0)
        assertEquals(shuffled.speed, audioState.speed)
        assertEquals(shuffled.stopped, false)
    }
}