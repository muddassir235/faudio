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
        assertTrue(audio should stop)
        assertTrue(audio should start)
    }

    @Test
    @Repeat(repetitions)
    fun testAudioStartPause() = afterAudioStart {
        assertTrue(audio should pause)
    }

    @Test
    @Repeat(repetitions)
    fun testAudioPauseStart() = afterAudioStart {
        assertTrue(audio should pause)
        assertTrue(audio should start)
    }

    @Test
    @Repeat(repetitions)
    fun testAudioPauseStop() = afterAudioStart {
        assertTrue(audio should pause)
        assertTrue(audio should stop)
    }

    @Test
    @Repeat(repetitions)
    fun testAudioStartStop() = afterAudioStart {
        assertTrue(audio should stop)
    }

    @Test
    @Repeat(repetitions)
    fun testAudioNext() = afterAudioStart {
        assertTrue(audio should moveToNext)
    }


    @Test
    @Repeat(repetitions)
    fun testAudioPrev() = afterAudioStart {
        assertTrue(audio should moveToPrev)
    }

    @Test
    @Repeat(repetitions)
    fun testAudioSeekTo() = afterAudioStart {
        assertTrue(audio should {
            seekTo(it, 100000)
        })
    }

    @Test
    @Repeat(repetitions)
    fun testMoveToIndex() = afterAudioStart {
        assertTrue(audio should {
            moveToIndex(it, 2)
        })
    }

    @Test
    @Repeat(repetitions)
    fun testRestart() = afterAudioStart {
        assertTrue(audio should restart)
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

            background {
                assertTrue(audio should {
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
    }

    private fun afterAudioStart(task: (suspend (Unit)->Unit)) {
        runOnMainThread {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            audio = Audio(context)

            val uris = uris(
                "https://audio-samples.github.io/samples/mp3/blizzard_biased/sample-5.mp3",
                "https://www.guggenheim.org/wp-content/uploads/2018/02/110443.mp3",
                "https://audio-samples.github.io/samples/mp3/blizzard_primed/sample-1.mp3"
            )

            background {
                assertTrue(audio.setState(ExpectedAudioState.defaultStateWithUris(uris)))
                assertTrue(audio should start)
                assertTrue(audio should moveToNext)
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