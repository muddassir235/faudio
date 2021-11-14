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
    fun testActualDownloadItemHashing() {
        val uriA = makeUri()
        val uriB = uriA
        val uriC = makeUri()

        val actualStateItemA = ActualDownloadItemState(uriA, 100.0f)
        val actualStateItemB = ActualDownloadItemState(uriB, 50.0f)

        val mapActual = mutableMapOf<ActualDownloadItemState, Boolean>()
        mapActual[actualStateItemA] = true
        mapActual[actualStateItemB] = true

        assertTrue(mapActual.size == 1)

        val actualStateItemC = ActualDownloadItemState(uriC, 100.0f)
        mapActual[actualStateItemC] = true

        assertTrue(mapActual.size ==2)
    }

    @Test
    fun testExpectDownloadItemHashing() {
        val uriA = makeUri()
        val uriB = uriA
        val uriC = makeUri()

        val expectedStateItemA = ExpectedDownloadItemState(uriA)
        val expectedStateItemB = ExpectedDownloadItemState(uriB)

        val mapExpected = mutableMapOf<ExpectedDownloadItemState, Boolean>()
        mapExpected[expectedStateItemA] = true
        mapExpected[expectedStateItemB] = true

        assertTrue(mapExpected.size == 1)

        val expectedStateItemC = ExpectedDownloadItemState(uriC)
        mapExpected[expectedStateItemC] = true

        assertTrue(mapExpected.size ==2)
    }

    @Test
    fun testActualDownloadItemListEquality() {
        val uri = makeUri()

        val actualStateItemA = ActualDownloadItemState(uri, 100.0f)
        val actualStateItemB = ActualDownloadItemState(uri, 50.0f)

        val actualStateItemListA = listOf(actualStateItemA)
        val actualStateItemListB = listOf(actualStateItemB)

        assertTrue(actualStateItemListA == actualStateItemListB)
    }

    @Test
    fun testExpectedDownloadItemListEquality() {
        val uri = makeUri()

        val expectedStateItemA = ExpectedDownloadItemState(uri)
        val expectedStateItemB = ExpectedDownloadItemState(uri)

        val expectedStateItemListA = listOf(expectedStateItemA)
        val expectedStateItemListB = listOf(expectedStateItemB)

        assertTrue(expectedStateItemListA == expectedStateItemListB)
    }

    @Test
    fun testActualExpectedDownloadItemListComparison() {
        val uriA = makeUri()

        val actualStateItemA = ActualDownloadItemState(uriA, 100.0f)
        val expectedStateItem = ExpectedDownloadItemState(uriA)

        val actualStateItemList = listOf(actualStateItemA)
        val expectedStateItemList = listOf(expectedStateItem)

        assertTrue(actualStateItemList == expectedStateItemList)
    }

    @Test
    fun testActualDownloadItemListInequality() {
        val uriA = makeUri()
        val uriB = makeUri()

        val actualStateItemA = ActualDownloadItemState(uriA, 100.0f)
        val actualStateItemB = ActualDownloadItemState(uriB, 100.0f)

        val actualStateItemListA = listOf(actualStateItemA)
        val actualStateItemListB = listOf(actualStateItemB)

        assertFalse(actualStateItemListA == actualStateItemListB)
    }

    @Test
    fun testExpectedDownloadItemListInequality() {
        val uriA = makeUri()
        val uriB = makeUri()

        val expectedStateItemA = ExpectedDownloadItemState(uriA)
        val expectedStateItemB = ExpectedDownloadItemState(uriB)

        val expectedStateItemListA = listOf(expectedStateItemA)
        val expectedStateItemListB = listOf(expectedStateItemB)

        assertFalse(expectedStateItemListA == expectedStateItemListB)
    }

    @Test
    fun testActualExpectedDownloadItemListInequality() {
        val uriA = makeUri()
        val uriB = makeUri()

        val actualStateItem = ActualDownloadItemState(uriA, 100.0f)
        val expectedStateItem = ExpectedDownloadItemState(uriB)

        val actualStateItemList = listOf(actualStateItem)
        val expectedStateItemList = listOf(expectedStateItem)

        assertFalse(actualStateItemList == expectedStateItemList)
    }

    @Test
    fun testDownloadStates() {
        // A
        val uriA = makeUri()

        val actualStateItemA = ActualDownloadItemState(uriA, 100.0f)
        val expectedStateItemA = ExpectedDownloadItemState(uriA)

        val actualStateA = ActualDownloadState(listOf(actualStateItemA), false)
        val expectedStateA = ExpectedDownloadState(listOf(expectedStateItemA), false)

        // B
        val uriB = uriA

        val actualStateItemB = ActualDownloadItemState(uriB, 100.0f)
        val expectedStateItemB = ExpectedDownloadItemState(uriB)

        val actualStateB = ActualDownloadState(listOf(actualStateItemB), true)
        val expectedStateB = ExpectedDownloadState(listOf(expectedStateItemB), true)

        // C
        val uriC = makeUri()

        val actualStateItemC = ActualDownloadItemState(uriC, 100.0f)
        val expectedStateItemC = ExpectedDownloadItemState(uriC)

        val actualStateC = ActualDownloadState(listOf(actualStateItemC), true)
        val expectedStateC = ExpectedDownloadState(listOf(expectedStateItemC), true)

        // Checks
        assertTrue(actualStateA.equals(expectedStateA))
        assertTrue(actualStateB.equals(expectedStateB))
        assertFalse(actualStateA == actualStateB)
        assertFalse(actualStateA.equals(expectedStateB))
        assertFalse(expectedStateA == expectedStateB)
        assertFalse(actualStateB.equals(expectedStateA))
        assertFalse(actualStateB == actualStateC)
        assertFalse(expectedStateB == expectedStateC)
    }

    @Test
    fun testActualDownloadStateHashing() {
        val uriA = makeUri()
        val uriB = uriA
        val uriC = makeUri()

        val actualStateItemA = ActualDownloadItemState(uriA, 100.0f)
        val actualStateItemB = ActualDownloadItemState(uriB, 100.0f)
        val actualStateItemC = ActualDownloadItemState(uriC, 100.0f)

        val actualStateA = ActualDownloadState(listOf(actualStateItemA), false)
        val actualStateB = ActualDownloadState(listOf(actualStateItemB), true)
        val actualStateC = ActualDownloadState(listOf(actualStateItemC), true)
        val actualStateCAlt = ActualDownloadState(listOf(actualStateItemC), true)

        val mapActual = mutableMapOf<ActualDownloadState, Boolean>()

        mapActual[actualStateA] = true
        mapActual[actualStateB] = true
        mapActual[actualStateC] = true
        mapActual[actualStateCAlt] = true

        assertTrue(mapActual.size == 3)
    }

    @Test
    fun testExpectedDownloadStateHashing() {
        val uriA = makeUri()
        val uriB = uriA
        val uriC = makeUri()

        val expectedStateItemA = ExpectedDownloadItemState(uriA)
        val expectedStateItemB = ExpectedDownloadItemState(uriB)
        val expectedStateItemC = ExpectedDownloadItemState(uriC)

        val expectedStateA = ExpectedDownloadState(listOf(expectedStateItemA), false)
        val expectedStateB = ExpectedDownloadState(listOf(expectedStateItemB), true)
        val expectedStateC = ExpectedDownloadState(listOf(expectedStateItemC), true)
        val expectedStateCAlt = ExpectedDownloadState(listOf(expectedStateItemC), true)

        val mapExpected = mutableMapOf<ExpectedDownloadState, Boolean>()

        mapExpected[expectedStateA] = true
        mapExpected[expectedStateB] = true
        mapExpected[expectedStateC] = true
        mapExpected[expectedStateCAlt] = true

        assertTrue(mapExpected.size == 3)
    }
}