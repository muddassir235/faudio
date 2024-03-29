package com.muddassir.faudio

import android.net.Uri
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Uri::class)
@PowerMockIgnore("jdk.internal.reflect.*")
class TestAudioMappings {
    @Test
    fun testUriToMediaItem() {
        val uri = makeUri()
        val mediaItem = uriToMediaItem(uri)

        assertTrue(uri == mediaItem.playbackProperties?.uri)
    }

    @Test
    fun testActualAudioItemToExpectedAudioItem() {
        val uri = makeUri()

        val actualAudioItem = ActualAudioItem(uri, true, false, 50.0f)
        val expectedAudioItem = actualToExpectedItem(actualAudioItem)

        assertTrue(actualAudioItem.uri == expectedAudioItem.uri)
        assertTrue(actualAudioItem.download == expectedAudioItem.download)

        assertTrue(actualAudioItem.equals(expectedAudioItem))
    }

    @Test
    fun testExpectedToActualState() {
        val uri = makeUri()

        val expectedAudioItem = ExpectedAudioItem(uri, true)
        val expectedState = ExpectedAudioState(
            listOf(expectedAudioItem), 0, false, 0L, 1.0f,
            false)

        val actualState = expectedToActualState(expectedState)

        assertTrue(actualState.equals(expectedState))
        assertTrue(expectedState.equals(actualState))
    }
}
