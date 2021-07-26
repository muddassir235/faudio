package com.muddassir.faudio.downloads

import android.net.Uri
import junit.framework.TestCase.assertFalse
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
class TestDownloadStates {
    @Before
    fun setup() {
        PowerMockito.mockStatic(Uri::class.java)
        val uri: Uri = PowerMockito.mock(Uri::class.java)

        PowerMockito.`when`<Any>(Uri::class.java, "parse", Matchers.anyString()).thenReturn(uri)
    }

    @Test
    fun testDownloadItemEquals() {
        val uriA = Uri.parse("https://site.com/audioA.mp3")

        val actualStateItemA = ActualDownloadItemState(uriA, 100.0f)
        val actualStateItemB = ActualDownloadItemState(uriA, 50.0f)

        val expectedStateItem = ExpectedDownloadItemState(uriA)

        assertTrue(actualStateItemA.equals(expectedStateItem))
        assertTrue(actualStateItemB.equals(expectedStateItem))
        assertTrue(actualStateItemA == actualStateItemB)
    }

    @Test
    fun testDownloadItemHashing() {
        val uriA = Uri.parse("https://site.com/audioA.mp3")

        val actualStateItemA = ActualDownloadItemState(uriA, 100.0f)
        val actualStateItemB = ActualDownloadItemState(uriA, 50.0f)

        val map = mutableMapOf<ActualDownloadItemState, Boolean>()
        map[actualStateItemA] = true
        map[actualStateItemB] = true

        assertTrue(map.size == 1)
    }

    @Test
    fun testDownloadItemList() {
        val uriA = Uri.parse("https://site.com/audioA.mp3")

        val actualStateItemA = ActualDownloadItemState(uriA, 100.0f)
        val expectedStateItem = ExpectedDownloadItemState(uriA)

        val actualStateItemList = listOf(actualStateItemA)
        val expectedStateItemList = listOf(expectedStateItem)

        assertTrue(actualStateItemList == expectedStateItemList)
    }

    @Test
    fun testDownloadStates() {
        val uriA = Uri.parse("https://site.com/audioA.mp3")

        val actualStateItem = ActualDownloadItemState(uriA, 100.0f)
        val expectedStateItem = ExpectedDownloadItemState(uriA)

        val actualStateA = ActualDownloadState(listOf(actualStateItem), false)
        val expectedStateA = ExpectedDownloadState(listOf(expectedStateItem), false)

        val actualStateB = ActualDownloadState(listOf(actualStateItem), true)
        val expectedStateB = ExpectedDownloadState(listOf(expectedStateItem), true)

        assertTrue(actualStateA.equals(expectedStateA))
        assertTrue(actualStateB.equals(expectedStateB))
        assertFalse(actualStateA == actualStateB)
        assertFalse(actualStateA.equals(expectedStateB))
        assertFalse(expectedStateA == expectedStateB)
        assertFalse(actualStateB.equals(expectedStateA))
    }

    @Test
    fun testDownloadHashing() {
        val uriA = Uri.parse("https://site.com/audioA.mp3")

        val actualStateItem = ActualDownloadItemState(uriA, 100.0f)
        val expectedStateItem = ExpectedDownloadItemState(uriA)

        val actualStateA = ActualDownloadState(listOf(actualStateItem), false)
        val actualStateB = ActualDownloadState(listOf(actualStateItem), true)

        val mapActual = mutableMapOf<ActualDownloadState, Boolean>()

        mapActual[actualStateA] = true
        mapActual[actualStateB] = true

        assertTrue(mapActual.size == 2)

        val expectedStateA = ExpectedDownloadState(listOf(expectedStateItem), false)
        val expectedStateB = ExpectedDownloadState(listOf(expectedStateItem), true)

        val mapExpected = mutableMapOf<ExpectedDownloadState, Boolean>()

        mapExpected[expectedStateA] = true
        mapExpected[expectedStateB] = true
    }
}