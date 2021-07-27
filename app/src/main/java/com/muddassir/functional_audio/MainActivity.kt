package com.muddassir.functional_audio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.muddassir.faudio.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        audioLibrary()
    }

    private fun audioLibrary() {
        val uris = uris(
            "https://server11.mp3quran.net/sds/001.mp3",
            "https://server11.mp3quran.net/sds/002.mp3",
            "https://server11.mp3quran.net/sds/003.mp3"
        )

        val audio = Audio(this)

        lifecycleScope.launch {
            if(audio.setState(ExpectedAudioState.defaultStateWithUris(uris))) {
                audio should (start then download)
                delay(10000)
                audio should stop
                delay(10000)
                audio should start
            }
        }

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