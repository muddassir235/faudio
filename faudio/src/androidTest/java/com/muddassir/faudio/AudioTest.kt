package com.muddassir.faudio

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.muddassir.kmacros.delay
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

const val repetitions = 3

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

    lateinit var audio: Audio

    /**
     * Test the following state transitions
     *
     *
     * reciteFrom      : _ -> 60 seconds
     * changeQari      : 0 -> 2
     * changeSurah     : 3 -> 10
     * reciteFromStart : _ -> 0 seconds
     */
    @Test
    fun testAudioStopStart() {
        afterAudioStart {
            background {
                assertTrue(audio.changeState(stop))
                assertTrue(audio.changeState(start))
            }
        }
    }

    @Test
    fun testAudioStartPause() {
        afterAudioStart {
            background {
                assertTrue(audio.changeState(pause))
            }
        }
    }

    @Test
    fun testAudioPauseStart() {
        afterAudioStart {
            background {
                assertTrue(audio.changeState(pause))
                assertTrue(audio.changeState(start))
            }
        }
    }

    @Test
    fun testAudioPauseStop() {
        afterAudioStart {
            background {
                assertTrue(audio.changeState(pause))
                assertTrue(audio.changeState(stop))
            }
        }
    }

    @Test
    fun testAudioStartStop() {
        afterAudioStart {
            background {
                assertTrue(audio.changeState(stop))
            }
        }
    }

    @Test
    fun testAudioNext() {
        afterAudioStart {
            background {
                assertTrue(audio.changeState(next))
            }
        }
    }

    @Test
    fun testAudioPrev() {
        afterAudioStart {
            background {
                assertTrue(audio.changeState(next))
                assertTrue(audio.changeState(prev))
            }
        }
    }

    @Test
    fun testAudio() {
        afterAudioStart {
            val otherUris = arrayOf(
                Uri.parse("https://server11.mp3quran.net/sds/004.mp3"),
                Uri.parse("https://server11.mp3quran.net/sds/005.mp3"),
                Uri.parse("https://server11.mp3quran.net/sds/006.mp3")
            )

            background {
                assertTrue(audio.changeState {
                    AudioState(
                        otherUris,
                        it.index,
                        it.paused,
                        0,
                        it.speed,
                        0,
                        0,
                        it.stopped,
                        it.error
                    )
                })
            }
        }
    }

    private fun afterAudioStart(task: ((Unit)->Unit)) {
        runOnMainThread {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            audio = Audio(context, GlobalScope)

            val uris = arrayOf(
                Uri.parse("https://server11.mp3quran.net/sds/001.mp3"),
                Uri.parse("https://server11.mp3quran.net/sds/002.mp3"),
                Uri.parse("https://server11.mp3quran.net/sds/003.mp3")
            )

            background {
                assertTrue(audio.setState(AudioState.defaultStateWithUris(uris)))
                assertTrue(audio.changeState(start))
            }

            delay(5000) { task.invoke(Unit) }
        }
    }

    fun ui( action:(suspend () -> Unit)) {
        GlobalScope.launch(Dispatchers.Main) {
            action()
        }
    }

    fun background( action:(suspend () -> Unit)) {
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