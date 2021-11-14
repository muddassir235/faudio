package com.muddassir.faudio.downloads

import android.net.Uri
import com.muddassir.faudio.makeUri
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Uri::class)
@PowerMockIgnore("jdk.internal.reflect.*")
class TestDownloadFunctions {

    @Test
    fun testAddDownloadNew() {
        val uriA = makeUri()
        val uriB = makeUri()

        val actualState = ActualDownloadState(listOf(ActualDownloadItemState(uriA, 1.0f)),
            false)

        val expectedState = addDownload(actualState, uriB)

        assertTrue(actualState.paused == expectedState.paused)
        assertTrue(expectedState.downloads.size == actualState.downloads.size + 1)
    }

    @Test
    fun testAddDownloadExiting() {
        val uriA = makeUri()

        val actualState = ActualDownloadState(listOf(ActualDownloadItemState(uriA, 1.0f)),
            false)

        val expectedState = addDownload(actualState, uriA)

        assertTrue(actualState.paused == expectedState.paused)
        assertTrue(expectedState.downloads.size == actualState.downloads.size)
    }

    @Test
    fun testStopDownload() {
        val uri = makeUri()

        val actualState = ActualDownloadState(listOf(ActualDownloadItemState(uri, 1.0f)),
            false)

        val expectedState = stopDownload(actualState, uri)

        assertTrue(actualState.paused == expectedState.paused)
        assertTrue(expectedState.downloads.size == actualState.downloads.size - 1)
    }

    @Test
    fun pauseDownload() {
        val uri = makeUri()

        val actualState = ActualDownloadState(listOf(ActualDownloadItemState(uri, 1.0f)),
            false)

        val expectedState = pause(actualState)

        assertTrue(expectedState.paused)
    }

    @Test
    fun resumeDownload() {
        val uri = makeUri()

        val actualState = ActualDownloadState(listOf(ActualDownloadItemState(uri, 1.0f)),
            false)

        val expectedState = pause(actualState)

        assertTrue(expectedState.paused)
    }
}