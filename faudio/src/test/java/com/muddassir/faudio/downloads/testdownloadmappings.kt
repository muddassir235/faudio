package com.muddassir.faudio.downloads

import android.net.Uri
import com.muddassir.faudio.makeUri
import junit.framework.TestCase.assertTrue
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
class TestDownloadMappings {

    @Test
    fun testExpectedDownloadStateToDownloadRequest() {
        val uri = makeUri()

        val expectedStateItem = ExpectedDownloadItemState(uri)
        val downloadRequest = expectedDownloadStateToDownloadRequest(expectedStateItem)

        assertTrue(downloadRequest.uri == expectedStateItem.uri)
    }

    @Test
    fun testActualToExpected() {
        val uri = makeUri()

        val actualDownloadItemState = ActualDownloadItemState(uri, 100.0f)

        val expectedDownloadItemState = actualToExpected(actualDownloadItemState)
        assertTrue(actualDownloadItemState.uri == expectedDownloadItemState.uri)
        assertTrue(actualDownloadItemState.equals(expectedDownloadItemState))
    }
}