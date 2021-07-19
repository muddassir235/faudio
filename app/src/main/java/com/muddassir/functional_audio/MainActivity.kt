package com.muddassir.functional_audio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.muddassir.faudio.*
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
            if(audio.setState(ExpectedState.defaultStateWithUris(uris))) {
                audio.changeState(start)
                delay(10000)
                audio.changeState(stop)
                delay(10000)
                audio.changeState(start)
            }
        }
    }
}