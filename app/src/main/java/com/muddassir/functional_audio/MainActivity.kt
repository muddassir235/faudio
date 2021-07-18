package com.muddassir.functional_audio

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.MediaItem
import com.muddassir.faudio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        audioLibrary()
    }

    private fun audioProducer() {
        val uris = arrayOf(
            Uri.parse("https://server11.mp3quran.net/sds/001.mp3"),
            Uri.parse("https://server11.mp3quran.net/sds/002.mp3"),
            Uri.parse("https://server11.mp3quran.net/sds/003.mp3")
        )

        val producer = AudioProducerBuilder(this).build()
        producer.setMediaItems(uris.map{ MediaItem.fromUri(it) })
        producer.prepare()

        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                producer.play()
            }

            delay(10000)

            withContext(Dispatchers.Main) {
                producer.stop()
            }

            delay(10000)

            withContext(Dispatchers.Main) {
                producer.play()
            }
        }
    }

    private fun audioLibrary() {
        val uris = arrayOf(
            Uri.parse("https://server11.mp3quran.net/sds/001.mp3"),
            Uri.parse("https://server11.mp3quran.net/sds/002.mp3"),
            Uri.parse("https://server11.mp3quran.net/sds/003.mp3")
        )

        val audio = Audio(this, lifecycleScope)

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