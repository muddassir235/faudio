package com.muddassir.functional_audio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.muddassir.faudio.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        audioLibrary()
    }

    private fun audioLibrary() {
        val audio = listOf(
            "https://server11.mp3quran.net/sds/001.mp3",
            "https://server11.mp3quran.net/sds/002.mp3",
            "https://server11.mp3quran.net/sds/003.mp3"
        ) asAudioWith(this)

        audio shouldPerform {
            this needsTo (start then download)

            delay(10000)
            this needsTo stop

            delay(10000)
            this needsTo start
        }

        audio should (start then download)

        audio.state.observe(this) {
            if(it.items.isNotEmpty()) {
                Log.e(MainActivity::class.simpleName, "Audio State: $it")
                val downloadPaused = it.items[it.index].downloadPaused
                val downloadProgress = it.items[it.index].downloadProgress

                textView.text = "Paused: $downloadPaused, Download Progress: $downloadProgress"
            }
        }
    }
}