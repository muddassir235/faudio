package com.muddassir.faudio

import android.net.Uri

data class AudioState(
    val uris                 : Array<Uri>,
    val index                : Int,
    val paused               : Boolean,
    val progress             : Long,
    val speed                : Float,
    val bufferedPosition     : Long,
    val currentIndexDuration : Long,
    val stopped              : Boolean,
    val error                : String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AudioState

        val progressTolerance = 10000L

        if (!uris.contentEquals(other.uris)) return false
        if (index != other.index) return false
        if (paused != other.paused) return false
        if (progress + progressTolerance < other.progress ||
            progress - progressTolerance > other.progress) return false
        if (speed != other.speed) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uris.contentHashCode()
        result = 31 * result + index
        result = 31 * result + paused.hashCode()
        result = 31 * result + ((progress+5000/10000)*10000).hashCode()
        result = 31 * result + speed.hashCode()

        return result
    }

    fun change(action: (AudioState) -> AudioState): AudioState {
        return action(this)
    }

    companion object {
        val DEFAULT = AudioState(arrayOf(), 0, true, 0, 1.0f,
            0, 0, true, null)

        fun defaultStateWithUris(uris: Array<Uri>): AudioState {
            return AudioState(
                uris,
                DEFAULT.index,
                DEFAULT.paused,
                DEFAULT.progress,
                DEFAULT.speed,
                DEFAULT.bufferedPosition,
                DEFAULT.currentIndexDuration,
                DEFAULT.stopped,
                DEFAULT.error
            )
        }
    }
}