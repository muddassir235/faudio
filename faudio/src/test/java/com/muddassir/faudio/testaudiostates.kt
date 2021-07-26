package com.muddassir.faudio

import android.net.Uri
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
class TestAudioStates {
    @Test
    fun testActualAudioItemEquality() {
        val uriA = makeUri()
        val uriB = uriA

        val actualItemA = ActualAudioItem(uriA, false, true, 10.0f)
        val actualItemB = ActualAudioItem(uriB, true, false, 20.0f)

        assertTrue(actualItemA == actualItemB)
    }

    @Test
    fun testActualAudioItemInequality() {
        val uriA = makeUri()
        val uriB = makeUri()

        val actualItemA = ActualAudioItem(uriA, false, true, 10.0f)
        val actualItemB = ActualAudioItem(uriB, false, true, 10.0f)

        assertFalse(actualItemA == actualItemB)
    }

    @Test
    fun testActualAudioItemHashing() {
        val uriA = makeUri()
        val uriB = uriA
        val uriC = makeUri()

        val actualItemA = ActualAudioItem(uriA, false, true, 10.0f)
        val actualItemB = ActualAudioItem(uriB, true, false, 20.0f)
        val actualItemC = ActualAudioItem(uriC, false, true, 10.0f)

        val map = HashMap<ActualAudioItem, Boolean>()
        map[actualItemA] = true
        map[actualItemB] = true
        map[actualItemC] = true

        assertTrue(map.size == 2)
    }

    @Test
    fun testExpectedAudioItemEquality() {
        val uriA = makeUri()
        val uriB = uriA

        val expectedItemA = ExpectedAudioItem(uriA, false)
        val expectedItemB = ExpectedAudioItem(uriB, true)

        assertTrue(expectedItemA == expectedItemB)
    }

    @Test
    fun testExpectedAudioItemInequality() {
        val uriA = makeUri()
        val uriB = makeUri()

        val expectedItemA = ExpectedAudioItem(uriA, false)
        val expectedItemB = ExpectedAudioItem(uriB, false)

        assertFalse(expectedItemA == expectedItemB)
    }

    @Test
    fun testExpectedAudioItemHashing() {
        val uriA = makeUri()
        val uriB = uriA
        val uriC = makeUri()

        val expectedItemA = ExpectedAudioItem(uriA, false)
        val expectedItemB = ExpectedAudioItem(uriB, true)
        val expectedItemC = ExpectedAudioItem(uriC, false)

        val map = HashMap<ExpectedAudioItem, Boolean>()
        map[expectedItemA] = true
        map[expectedItemB] = true
        map[expectedItemC] = true

        assertTrue(map.size == 2)
    }

    @Test
    fun testActualExpectedAudioItemEquality() {
        val uriA = makeUri()
        val uriB = uriA

        val actualItem = ActualAudioItem(uriA, false, true, 10.0f)
        val expectedItem = ExpectedAudioItem(uriB, true)

        assertTrue(actualItem.equals(expectedItem))
        assertTrue(expectedItem.equals(actualItem))
    }

    @Test
    fun testActualExpectedAudioItemInequality() {
        val uriA = makeUri()
        val uriB = makeUri()

        val actualItem = ActualAudioItem(uriA, false, true, 10.0f)
        val expectedItem = ExpectedAudioItem(uriB, false)

        assertFalse(actualItem.equals(expectedItem))
        assertFalse(expectedItem.equals(actualItem))
    }

    @Test
    fun testActualAudioStateEquality() {
        val uriA = makeUri()
        val uriB = uriA

        val actualAudioItemA = ActualAudioItem(uriA, true, false, 10.0f)
        val actualAudioItemB = ActualAudioItem(uriB, false, true, 20.0f)

        val actualAudioStateA = ActualAudioState(
            listOf(actualAudioItemA),
            0,
            false,
            2000L,
            1.0f,
            10000L,
            100000L,
            false,
            null
        )

        val actualAudioStateB = ActualAudioState(
            listOf(actualAudioItemB),
            0,
            false,
            4000L,
            1.0f,
            15000L,
            120000L,
            true,
            "Error"
        )

        assertTrue(actualAudioStateA == actualAudioStateB)
    }

    @Test
    fun testActualAudioStateInequalityDueToUri() {
        val uriA = makeUri()
        val uriB = makeUri()

        val actualAudioItemA = ActualAudioItem(uriA, true, false, 10.0f)
        val actualAudioItemB = ActualAudioItem(uriB, false, true, 20.0f)

        val actualAudioStateA = ActualAudioState(
            listOf(actualAudioItemA),
            0,
            false,
            2000L,
            1.0f,
            10000L,
            100000L,
            false,
            null
        )

        val actualAudioStateB = ActualAudioState(
            listOf(actualAudioItemB),
            0,
            false,
            2000L,
            1.0f,
            10000L,
            100000L,
            false,
            null
        )

        assertFalse(actualAudioStateA == actualAudioStateB)
    }

    @Test
    fun testActualAudioStateInequalityDueToPaused() {
        val uriA = makeUri()
        val uriB = uriA

        val actualAudioItemA = ActualAudioItem(uriA, true, false, 10.0f)
        val actualAudioItemB = ActualAudioItem(uriB, false, true, 20.0f)

        val actualAudioStateA = ActualAudioState(
            listOf(actualAudioItemA),
            0,
            false,
            2000L,
            1.0f,
            10000L,
            100000L,
            false,
            null
        )

        val actualAudioStateB = ActualAudioState(
            listOf(actualAudioItemB),
            0,
            true,
            2000L,
            1.0f,
            10000L,
            100000L,
            false,
            null
        )

        assertFalse(actualAudioStateA == actualAudioStateB)
    }

    @Test
    fun testActualAudioStateInequalityDueToProgress() {
        val uriA = makeUri()
        val uriB = uriA

        val actualAudioItemA = ActualAudioItem(uriA, true, false, 10.0f)
        val actualAudioItemB = ActualAudioItem(uriB, false, true, 20.0f)

        val actualAudioStateA = ActualAudioState(
            listOf(actualAudioItemA),
            0,
            false,
            2000L,
            1.0f,
            10000L,
            100000L,
            false,
            null
        )

        val actualAudioStateB = ActualAudioState(
            listOf(actualAudioItemB),
            0,
            false,
            20000L,
            1.0f,
            10000L,
            100000L,
            false,
            null
        )

        assertFalse(actualAudioStateA == actualAudioStateB)
    }

    @Test
    fun testActualAudioStateInequalityDueToIndex() {
        val uriA = makeUri()
        val uriB = uriA
        val uriC = makeUri()

        val actualAudioItemA = ActualAudioItem(uriA, true, false, 10.0f)
        val actualAudioItemB = ActualAudioItem(uriB, false, true, 20.0f)
        val actualAudioItemC = ActualAudioItem(uriC, false, true, 20.0f)

        val actualAudioStateA = ActualAudioState(
            listOf(actualAudioItemA, actualAudioItemC),
            0,
            false,
            2000L,
            1.0f,
            10000L,
            100000L,
            false,
            null
        )

        val actualAudioStateB = ActualAudioState(
            listOf(actualAudioItemB, actualAudioItemC),
            1,
            false,
            2000L,
            1.0f,
            10000L,
            100000L,
            false,
            null
        )

        assertFalse(actualAudioStateA == actualAudioStateB)
    }

    @Test
    fun testActualAudioStateHashing() {
        val uriA = makeUri()
        val uriB = uriA
        val uriC = uriB
        val uriD = uriC
        val uriE = uriD
        val uriF = uriE
        val uriG = makeUri()

        val uriAdditional = makeUri()

        val actualAudioItemA = ActualAudioItem(uriA, false, false, 20.0f)
        val actualAudioItemB = ActualAudioItem(uriB, false, false, 20.0f)
        val actualAudioItemC = ActualAudioItem(uriC, false, false, 20.0f)
        val actualAudioItemD = ActualAudioItem(uriD, false, false, 20.0f)
        val actualAudioItemE = ActualAudioItem(uriE, false, false, 20.0f)
        val actualAudioItemF = ActualAudioItem(uriF, false, false, 20.0f)
        val actualAudioItemG = ActualAudioItem(uriG, false, false, 20.0f)

        val additionalActualAudioItem = ActualAudioItem(uriAdditional, false, false, 20.0f)

        val actualAudioStateA = ActualAudioState(
            listOf(actualAudioItemA, additionalActualAudioItem),
            0,
            false,
            2000L,
            1.0f,
            10000L,
            100000L,
            false,
            null
        )

        val actualAudioStateB = ActualAudioState(
            listOf(actualAudioItemB, additionalActualAudioItem),
            0,
            false,
            4000L,
            1.0f,
            15000L,
            120000L,
            true,
            "Error"
        )


        val actualAudioStateC = ActualAudioState(
            listOf(actualAudioItemC, additionalActualAudioItem),
            0,
            false,
            14000L,
            1.0f,
            10000L,
            100000L,
            false,
            null
        )

        val actualAudioStateD = ActualAudioState(
            listOf(actualAudioItemD, additionalActualAudioItem),
            0,
            true,
            4000L,
            1.0f,
            10000L,
            100000L,
            false,
            null
        )

        val actualAudioStateE = ActualAudioState(
            listOf(actualAudioItemE, additionalActualAudioItem),
            0,
            false,
            4000L,
            2.0f,
            10000L,
            100000L,
            false,
            null
        )

        val actualAudioStateF = ActualAudioState(
            listOf(actualAudioItemF, additionalActualAudioItem),
            1,
            false,
            4000L,
            2.0f,
            10000L,
            100000L,
            false,
            null
        )

        val actualAudioStateG = ActualAudioState(
            listOf(actualAudioItemG, additionalActualAudioItem),
            0,
            false,
            4000L,
            2.0f,
            10000L,
            100000L,
            false,
            null
        )

        val map = HashMap<ActualAudioState, Boolean>()

        map[actualAudioStateA] = true
        map[actualAudioStateB] = true
        map[actualAudioStateC] = true
        map[actualAudioStateD] = true
        map[actualAudioStateE] = true
        map[actualAudioStateF] = true
        map[actualAudioStateG] = true

        assertTrue(map.size == 6)
    }

    @Test
    fun testExpectedAudioStateEquality() {
        val uriA = makeUri()
        val uriB = uriA

        val expectedAudioItemA = ExpectedAudioItem(uriA, true)
        val expectedAudioItemB = ExpectedAudioItem(uriB, false)

        val expectedAudioStateA = ExpectedAudioState(
            listOf(expectedAudioItemA),
            0,
            false,
            2000L,
            1.0f,
            false
        )

        val expectedAudioStateB = ExpectedAudioState(
            listOf(expectedAudioItemB),
            0,
            false,
            4000L,
            1.0f,
            true
        )

        assertTrue(expectedAudioStateA == expectedAudioStateB)
    }

    @Test
    fun testExpectedAudioStateInequalityDueToUri() {
        val uriA = makeUri()
        val uriB = makeUri()

        val expectedAudioItemA = ExpectedAudioItem(uriA, true)
        val expectedAudioItemB = ExpectedAudioItem(uriB, false)

        val expectedAudioStateA = ExpectedAudioState(
            listOf(expectedAudioItemA),
            0,
            false,
            2000L,
            1.0f,
            false
        )

        val expectedAudioStateB = ExpectedAudioState(
            listOf(expectedAudioItemB),
            0,
            false,
            2000L,
            1.0f,
            false
        )

        assertFalse(expectedAudioStateA == expectedAudioStateB)
    }

    @Test
    fun testExpectedAudioStateInequalityDueToPaused() {
        val uriA = makeUri()
        val uriB = uriA

        val expectedAudioItemA = ExpectedAudioItem(uriA, true)
        val expectedAudioItemB = ExpectedAudioItem(uriB, false)

        val expectedAudioStateA = ExpectedAudioState(
            listOf(expectedAudioItemA),
            0,
            false,
            2000L,
            1.0f,
            false
        )

        val expectedAudioStateB = ExpectedAudioState(
            listOf(expectedAudioItemB),
            0,
            true,
            2000L,
            1.0f,
            false
        )

        assertFalse(expectedAudioStateA == expectedAudioStateB)
    }

    @Test
    fun testExpectedAudioStateInequalityDueToProgress() {
        val uriA = makeUri()
        val uriB = uriA

        val expectedAudioItemA = ExpectedAudioItem(uriA, true)
        val expectedAudioItemB = ExpectedAudioItem(uriB, false)

        val expectedAudioStateA = ExpectedAudioState(
            listOf(expectedAudioItemA),
            0,
            false,
            2000L,
            1.0f,
            false
        )

        val expectedAudioStateB = ExpectedAudioState(
            listOf(expectedAudioItemB),
            0,
            false,
            20000L,
            1.0f,
            false
        )

        assertFalse(expectedAudioStateA == expectedAudioStateB)
    }

    @Test
    fun testExpectedAudioStateInequalityDueToIndex() {
        val uriA = makeUri()
        val uriB = uriA
        val uriC = makeUri()

        val expectedAudioItemA = ExpectedAudioItem(uriA, true)
        val expectedAudioItemB = ExpectedAudioItem(uriB, false)
        val expectedAudioItemC = ExpectedAudioItem(uriC, false)

        val expectedAudioStateA = ExpectedAudioState(
            listOf(expectedAudioItemA, expectedAudioItemC),
            0,
            false,
            2000L,
            1.0f,
            false
        )

        val expectedAudioStateB = ExpectedAudioState(
            listOf(expectedAudioItemB, expectedAudioItemC),
            1,
            false,
            2000L,
            1.0f,
            false
        )

        assertFalse(expectedAudioStateA == expectedAudioStateB)
    }

    @Test
    fun testExpectedAudioStateHashing() {
        val uriA = makeUri()
        val uriB = uriA
        val uriC = uriB
        val uriD = uriC
        val uriE = uriD
        val uriF = uriE
        val uriG = makeUri()

        val uriAdditional = makeUri()

        val expectedAudioItemA = ExpectedAudioItem(uriA, false)
        val expectedAudioItemB = ExpectedAudioItem(uriB, false)
        val expectedAudioItemC = ExpectedAudioItem(uriC, false)
        val expectedAudioItemD = ExpectedAudioItem(uriD, false)
        val expectedAudioItemE = ExpectedAudioItem(uriE, false)
        val expectedAudioItemF = ExpectedAudioItem(uriF, false)
        val expectedAudioItemG = ExpectedAudioItem(uriG, false)

        val additionalExpectedAudioItem = ExpectedAudioItem(uriAdditional, false)

        val expectedAudioStateA = ExpectedAudioState(
            listOf(expectedAudioItemA, additionalExpectedAudioItem),
            0,
            false,
            2000L,
            1.0f,
            false
        )

        val expectedAudioStateB = ExpectedAudioState(
            listOf(expectedAudioItemB, additionalExpectedAudioItem),
            0,
            false,
            4000L,
            1.0f,
            true
        )


        val expectedAudioStateC = ExpectedAudioState(
            listOf(expectedAudioItemC, additionalExpectedAudioItem),
            0,
            false,
            14000L,
            1.0f,
            false
        )

        val expectedAudioStateD = ExpectedAudioState(
            listOf(expectedAudioItemD, additionalExpectedAudioItem),
            0,
            true,
            4000L,
            1.0f,
            false
        )

        val expectedAudioStateE = ExpectedAudioState(
            listOf(expectedAudioItemE, additionalExpectedAudioItem),
            0,
            false,
            4000L,
            2.0f,
            false
        )

        val expectedAudioStateF = ExpectedAudioState(
            listOf(expectedAudioItemF, additionalExpectedAudioItem),
            1,
            false,
            4000L,
            2.0f,
            false
        )

        val expectedAudioStateG = ExpectedAudioState(
            listOf(expectedAudioItemG, additionalExpectedAudioItem),
            0,
            false,
            4000L,
            2.0f,
            false
        )

        val map = HashMap<ExpectedAudioState, Boolean>()

        map[expectedAudioStateA] = true
        map[expectedAudioStateB] = true
        map[expectedAudioStateC] = true
        map[expectedAudioStateD] = true
        map[expectedAudioStateE] = true
        map[expectedAudioStateF] = true
        map[expectedAudioStateG] = true

        assertTrue(map.size == 6)
    }

    @Test
    fun testActualExpectedAudioStateEquality() {
        val uriA = makeUri()
        val uriB = uriA

        val actualAudioItem = ActualAudioItem(uriA, true, false, 10.0f)
        val expectedAudioItem = ExpectedAudioItem(uriB, false)

        val actualAudioState = ActualAudioState(
            listOf(actualAudioItem),
            0,
            false,
            2000L,
            1.0f,
            10000L,
            100000L,
            false,
            null
        )

        val expectedAudioState = ExpectedAudioState(
            listOf(expectedAudioItem),
            0,
            false,
            4000L,
            1.0f,
            true
        )

        assertTrue(actualAudioState.equals(expectedAudioState))
        assertTrue(expectedAudioState.equals(actualAudioState))
    }

    @Test
    fun testActualExpectedAudioStateInequalityDueToUri() {
        val uriA = makeUri()
        val uriB = makeUri()

        val actualAudioItem = ActualAudioItem(uriA, true, false, 10.0f)
        val expectedAudioItem = ExpectedAudioItem(uriB, true)

        val actualAudioState = ActualAudioState(
            listOf(actualAudioItem),
            0,
            false,
            2000L,
            1.0f,
            8000L,
            100000L,
            false,
            null
        )

        val expectedAudioState = ExpectedAudioState(
            listOf(expectedAudioItem),
            0,
            false,
            2000L,
            1.0f,
            false
        )

        assertFalse(actualAudioState.equals(expectedAudioState))
        assertFalse(expectedAudioState.equals(actualAudioState))
    }

    @Test
    fun testActualExpectedAudioStateInequalityDueToPaused() {
        val uriA = makeUri()
        val uriB = uriA

        val actualAudioItem = ActualAudioItem(uriA, true, false, 10.0f)
        val expectedAudioItem = ExpectedAudioItem(uriB, true)

        val actualAudioState = ActualAudioState(
            listOf(actualAudioItem),
            0,
            false,
            2000L,
            1.0f,
            8000L,
            100000L,
            false,
            null
        )

        val expectedAudioState = ExpectedAudioState(
            listOf(expectedAudioItem),
            0,
            true,
            2000L,
            1.0f,
            false
        )

        assertFalse(actualAudioState.equals(expectedAudioState))
        assertFalse(expectedAudioState.equals(actualAudioState))
    }

    @Test
    fun testActualExpectedAudioStateInequalityDueToProgress() {
        val uriA = makeUri()
        val uriB = uriA

        val actualAudioItem = ActualAudioItem(uriA, true, false, 10.0f)
        val expectedAudioItem = ExpectedAudioItem(uriB, true)

        val actualAudioState = ActualAudioState(
            listOf(actualAudioItem),
            0,
            false,
            2000L,
            1.0f,
            8000L,
            100000L,
            false,
            null
        )

        val expectedAudioState = ExpectedAudioState(
            listOf(expectedAudioItem),
            0,
            false,
            20000L,
            1.0f,
            false
        )

        assertFalse(actualAudioState.equals(expectedAudioState))
        assertFalse(expectedAudioState.equals(actualAudioState))
    }

    @Test
    fun testActualExpectedAudioStateInequalityDueToIndex() {
        val uriA = makeUri()
        val uriB = uriA
        val uriC = makeUri()

        val actualAudioItem = ActualAudioItem(uriA, true, false, 10.0f)
        val expectedAudioItem = ExpectedAudioItem(uriB, false)

        val otherActualAudioItem = ActualAudioItem(uriC, true, false, 10.0f)
        val otherExpectedAudioItem = ExpectedAudioItem(uriC, false)

        val actualAudioState = ActualAudioState(
            listOf(actualAudioItem, otherActualAudioItem),
            0,
            false,
            2000L,
            1.0f,
            8000L,
            100000L,
            false,
            null
        )

        val expectedAudioState = ExpectedAudioState(
            listOf(expectedAudioItem, otherExpectedAudioItem),
            1,
            false,
            2000L,
            1.0f,
            false
        )

        assertFalse(actualAudioState.equals(expectedAudioState))
        assertFalse(expectedAudioState.equals(actualAudioState))
    }
}