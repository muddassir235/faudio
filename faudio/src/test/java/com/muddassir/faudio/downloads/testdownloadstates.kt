package com.muddassir.faudio.downloads

import android.net.Uri
import com.muddassir.faudio.makeUri
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Uri::class)
@PowerMockIgnore("jdk.internal.reflect.*")
class TestDownloadStates {
    @Test
    fun testDownloadItemEquals() {
        val uriA = makeUri()

        val actualStateItemA = ActualDownloadItemState(uriA, 100.0f)
        val actualStateItemB = ActualDownloadItemState(uriA, 50.0f)

        val expectedStateItem = ExpectedDownloadItemState(uriA)

        assertTrue(actualStateItemA.equals(expectedStateItem))
        assertTrue(actualStateItemB.equals(expectedStateItem))
        assertTrue(actualStateItemA == actualStateItemB)
    }

    @Test
    fun testDownloadItemNotEquals() {
        val uriA = makeUri()
        val uriB = makeUri()

        val actualStateItemA = ActualDownloadItemState(uriA, 100.0f)
        val actualStateItemB = ActualDownloadItemState(uriB, 100.0f)

        val expectedStateItemA = ExpectedDownloadItemState(uriA)
        val expectedStateItemB = ExpectedDownloadItemState(uriB)

        assertFalse(actualStateItemA.equals(expectedStateItemB))
        assertFalse(actualStateItemB.equals(expectedStateItemA))
        assertFalse(actualStateItemA == actualStateItemB)
        assertFalse(expectedStateItemA == expectedStateItemB)
    }

    @Test
    fun testDownloadItemHashing() {
        val uriA = makeUri()
        val otherUri = makeUri()

        val actualStateItemA = ActualDownloadItemState(uriA, 100.0f)
        val actualStateItemB = ActualDownloadItemState(uriA, 50.0f)

        val mapActual = mutableMapOf<ActualDownloadItemState, Boolean>()
        mapActual[actualStateItemA] = true
        mapActual[actualStateItemB] = true

        assertTrue(mapActual.size == 1)


        val actualStateItemC = ActualDownloadItemState(otherUri, 100.0f)
        mapActual[actualStateItemC] = true

        assertTrue(mapActual.size ==2)

        val expectedStateItemA = ExpectedDownloadItemState(uriA)
        val expectedStateItemB = ExpectedDownloadItemState(uriA)

        val mapExpected = mutableMapOf<ExpectedDownloadItemState, Boolean>()
        mapExpected[expectedStateItemA] = true
        mapExpected[expectedStateItemB] = true

        assertTrue(mapExpected.size == 1)


        val expectedStateItemC = ExpectedDownloadItemState(otherUri)
        mapExpected[expectedStateItemC] = true

        assertTrue(mapExpected.size ==2)
    }

    @Test
    fun testDownloadItemList() {
        val uriA = makeUri()

        val actualStateItemA = ActualDownloadItemState(uriA, 100.0f)
        val expectedStateItem = ExpectedDownloadItemState(uriA)

        val actualStateItemList = listOf(actualStateItemA)
        val expectedStateItemList = listOf(expectedStateItem)

        assertTrue(actualStateItemList == expectedStateItemList)
    }

    @Test
    fun testDownloadStates() {
        val uriA = makeUri()
        val otherUri = makeUri()

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

        val actualStateC = ActualDownloadState(listOf(
            ActualDownloadItemState(otherUri, 100.0f)
        ), true)
        val expectedStateC = ExpectedDownloadState(listOf(
            ExpectedDownloadItemState(otherUri)), true)

        assertFalse(actualStateB == actualStateC)
        assertFalse(expectedStateB == expectedStateC)
    }

    @Test
    fun testDownloadHashing() {
        val uriA = makeUri()

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

        assertTrue(mapExpected.size == 2)
    }
}