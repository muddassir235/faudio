package com.muddassir.faudio

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

const val repetitions = 1

/**
 * Instrumented test, which will execute on an Android device.
 *
 * These tests require the device to be connected to the internet and mp3quran.net to be accessible
 * from the device.
 */
@RunWith(AndroidJUnit4::class)
class AudioTest {
    @Rule @JvmField
    var repeatRule: RepeatRule = RepeatRule()

    private lateinit var audio: Audio

    /**
     * Test the following state transitions
     * stop -> start
     * start -> pause
     * pause -> start
     * pause -> stop
     * start -> stop
     * next
     * prev
     * seekTo(100*1000)
     * moveToIndex(2)
     * restart
     * changeUris
     */
    @Test
    @Repeat(repetitions)
    fun testAudioStopStart() = afterAudioStart {
        assertTrue(audio needsTo stop)
        assertTrue(audio needsTo start)
    }

    @Test
    @Repeat(repetitions)
    fun testAudioStartPause() = afterAudioStart {
        assertTrue(audio needsTo pause)
    }

    @Test
    @Repeat(repetitions)
    fun testAudioPauseStart() = afterAudioStart {
        assertTrue(audio needsTo pause)
        assertTrue(audio needsTo start)
    }

    @Test
    @Repeat(repetitions)
    fun testAudioPauseStop() = afterAudioStart {
        assertTrue(audio needsTo pause)
        assertTrue(audio needsTo stop)
    }

    @Test
    @Repeat(repetitions)
    fun testAudioStartStop() = afterAudioStart {
        assertTrue(audio needsTo stop)
    }

    @Test
    @Repeat(repetitions)
    fun testAudioNext() = afterAudioStart {
        assertTrue(audio needsTo moveToNext)
    }


    @Test
    @Repeat(repetitions)
    fun testAudioPrev() = afterAudioStart {
        assertTrue(audio needsTo moveToPrev)
    }

    @Test
    @Repeat(repetitions)
    fun testAudioSeekTo() = afterAudioStart {
        assertTrue(audio needsTo {
            seekTo(it, 100000)
        })
    }

    @Test
    @Repeat(repetitions)
    fun testMoveToIndex() = afterAudioStart {
        assertTrue(audio needsTo {
            moveToIndex(it, 2)
        })
    }

    @Test
    @Repeat(repetitions)
    fun testRestart() = afterAudioStart {
        assertTrue(audio needsTo restart)
    }

    @Test
    @Repeat(repetitions)
    fun testAudioChangeUris() {
        afterAudioStart {
            val otherUris = uris(
                "https://audio-samples.github.io/samples/mp3/blizzard_unconditional/sample-5.mp3",
                "https://audio-samples.github.io/samples/mp3/blizzard_primed/sample-0.mp3",
                "https://audio-samples.github.io/samples/mp3/blizzard_unconditional/sample-0.mp3"
            )

            assertTrue(audio needsTo {
                ExpectedAudioState(
                    otherUris.map { uri -> ExpectedAudioItem(uri, false) },
                    it.index,
                    it.paused,
                    0,
                    it.speed,
                    it.stopped
                )
            })
        }
    }

    private fun afterAudioStart(task: (suspend (Unit)->Unit)) {
        runOnMainThread {
            val context = InstrumentationRegistry.getInstrumentation().targetContext

            audio = listOf(
                "https://audio-samples.github.io/samples/mp3/blizzard_biased/sample-5.mp3",
                "https://www.guggenheim.org/wp-content/uploads/2018/02/110443.mp3",
                "https://audio-samples.github.io/samples/mp3/blizzard_primed/sample-1.mp3"
            ) asAudioWith context

            audio shouldPerform {
                assertTrue(audio needsTo start)
                assertTrue(audio needsTo moveToNext)
                delay(5000)
                task.invoke(Unit)
            }
        }
    }

    private fun background( action:(suspend () -> Unit)) {
        GlobalScope.launch(Dispatchers.IO) {
            action()
        }
    }

    private fun runOnMainThread(task: ((Unit)->Unit)) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync { task.invoke(Unit) }
        Thread.sleep(15000)
    }

    private fun runOnMainThread(duration: Long, task: ((Unit)->Unit)) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync { task.invoke(Unit) }
        Thread.sleep(duration)
    }
}