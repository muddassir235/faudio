package com.muddassir.faudio

import android.net.Uri
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Uri::class)
@PowerMockIgnore("jdk.internal.reflect.*")
class TestAudioStateChangeTypes {
    private lateinit var audioState: ActualAudioState

    @Before
    fun setup() {
        PowerMockito.mockStatic(Uri::class.java)
        val uri: Uri = PowerMockito.mock(Uri::class.java)

        PowerMockito.`when`<Any>(Uri::class.java, "parse", Matchers.anyString()).thenReturn(uri)

        val audios = audios(
            "https://audio-samples.github.io/samples/mp3/blizzard_unconditional/sample-5.mp3",
            "https://audio-samples.github.io/samples/mp3/blizzard_primed/sample-0.mp3",
            "https://audio-samples.github.io/samples/mp3/blizzard_unconditional/sample-0.mp3"
        )

        audioState = ActualAudioState(
            audios = audios,
            index = 0,
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
    fun testDiffs() {
        val started = start(audioState)
        val actualStartedState = mockActualStateFromExpectedState(started)

        Assert.assertTrue(audioState.changeType(actualStartedState) == AudioStateChangeTypes.START)

        val startedAndDownloading = startAndDownload(audioState)
        val actualStartedAndDownloadingState = mockActualStateFromExpectedState(startedAndDownloading)

        Assert.assertTrue(audioState.changeType(actualStartedAndDownloadingState)
                == AudioStateChangeTypes.START_AND_DOWNLOAD)

        val downloading = downloadCurrent(audioState)
        val actualDownloadingState = mockActualStateFromExpectedState(downloading)

        Assert.assertTrue(audioState.changeType(actualDownloadingState)
                == AudioStateChangeTypes.DOWNLOAD_CURRENT)

        val paused = pause(actualStartedState)
        val actualPausedState = mockActualStateFromExpectedState(paused)

        Assert.assertTrue(actualStartedState.changeType(actualPausedState)
                == AudioStateChangeTypes.PAUSE)

        val stopped = stop(actualPausedState)
        val actualStoppedState = mockActualStateFromExpectedState(stopped)

        Assert.assertTrue(actualPausedState.changeType(actualStoppedState)
                == AudioStateChangeTypes.STOP)

        val next = next(actualStartedState)
        val actualNextState = mockActualStateFromExpectedState(next)

        Assert.assertTrue(actualStartedState.changeType(actualNextState)
                == AudioStateChangeTypes.NEXT)

        val nextAndDownloading = nextAndDownload(actualStartedState)
        val actualNextAndDownloadingState = mockActualStateFromExpectedState(nextAndDownloading)

        Assert.assertTrue(actualStartedState.changeType(actualNextAndDownloadingState)
                == AudioStateChangeTypes.NEXT_AND_DOWNLOAD)

        val prev = prev(actualNextState)
        val actualPrevState = mockActualStateFromExpectedState(prev)

        Assert.assertTrue(actualNextState.changeType(actualPrevState) == AudioStateChangeTypes.PREV)

        val prevAndDownloading = prevAndDownload(actualNextState)
        val actualPrevAndDownloadingState = mockActualStateFromExpectedState(prevAndDownloading)

        Assert.assertTrue(actualNextState.changeType(actualPrevAndDownloadingState)
                == AudioStateChangeTypes.PREV_AND_DOWNLOAD)

        val seeked = seekTo(actualStartedState, 100000)
        val actualSeekedState = mockActualStateFromExpectedState(seeked)

        Assert.assertTrue(actualStartedState.changeType(actualSeekedState)
                == AudioStateChangeTypes.SEEK)

        val moved = moveToIndex(actualStartedState, 2)
        val actualMovedState = mockActualStateFromExpectedState(moved)

        Assert.assertTrue(actualStartedState.changeType(actualMovedState)
                == AudioStateChangeTypes.MOVE_TO_INDEX)

        val movedAndDownloading = moveToIndexAndDownload(actualStartedState, 2)
        val actualMovedAndDownloadingState = mockActualStateFromExpectedState(movedAndDownloading)

        Assert.assertTrue(actualStartedState.changeType(actualMovedAndDownloadingState)
                == AudioStateChangeTypes.MOVE_TO_INDEX_AND_DOWNLOAD)

        val downloadIndex = downloadIndex(audioState, 2)
        val actualDownloadIndexState = mockActualStateFromExpectedState(downloadIndex)

        Assert.assertTrue(audioState.changeType(actualDownloadIndexState)
                == AudioStateChangeTypes.DOWNLOAD_INDEX)

        val restarted = restart(actualSeekedState)
        val actualRestartedState = mockActualStateFromExpectedState(restarted)

        Assert.assertTrue(actualSeekedState.changeType(actualRestartedState) == AudioStateChangeTypes.RESTART)

        val urisChangedState = ActualAudioState(
            emptyList(),
            audioState.index,
            audioState.paused,
            audioState.progress,
            audioState.speed,
            audioState.bufferedPosition,
            audioState.currentIndexDuration,
            audioState.stopped,
            audioState.error
        )
        Assert.assertTrue(audioState.changeType(urisChangedState) == AudioStateChangeTypes.URIS_CHANGED)

        Assert.assertTrue(actualSeekedState.changeType(actualSeekedState) == AudioStateChangeTypes.UNCHANGED)
    }

    private fun mockActualStateFromExpectedState(expectedState: ExpectedAudioState): ActualAudioState {
        return ActualAudioState(
            expectedState.audios.map {
                ActualAudioItem(it.uri, it.download, false, 0f)
            },
            expectedState.index,
            expectedState.paused,
            expectedState.progress,
            expectedState.speed,
            0L,
            0L,
            expectedState.stopped,
            null
        )
    }
}