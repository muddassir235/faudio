package com.muddassir.faudio

import android.net.Uri

data class ExpectedState(
    val uris: Array<Uri>,
    val index: Int,
    val paused: Boolean,
    val progress: Long,
    val speed: Float,
    val stopped: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if(javaClass != ExpectedState::class.java && javaClass != ActualState::class.java) {
            return false
        }

        when(other) {
            is ExpectedState -> {
                if (!uris.contentEquals(other.uris)) return false
                if (index != other.index) return false
                if (paused != other.paused) return false
                if (progress != other.progress) return false
                if (speed != other.speed) return false
                if (stopped != other.stopped) return false

                return true
            }
            is ActualState -> {
                val progressTolerance = 10000L

                if (!uris.contentEquals(other.uris)) return false
                if (index != other.index) return false
                if (paused != other.paused) return false
                if (progress + progressTolerance < other.progress ||
                    progress - progressTolerance > other.progress) return false
                if (speed != other.speed) return false

                return true
            }
            else -> return false
        }
    }

    override fun hashCode(): Int {
        var result = uris.contentHashCode()
        result = 31 * result + index
        result = 31 * result + paused.hashCode()
        result = 31 * result + progress.hashCode()
        result = 31 * result + speed.hashCode()
        result = 31 * result + stopped.hashCode()
        return result
    }

    companion object {
        val DEFAULT = ExpectedState(
            arrayOf(), 0, true, 0, 1.0f, true)

        fun defaultStateWithUris(uris: Array<Uri>): ExpectedState {
            return ExpectedState(
                uris,
                DEFAULT.index,
                DEFAULT.paused,
                DEFAULT.progress,
                DEFAULT.speed,
                DEFAULT.stopped
            )
        }
    }
}

data class ActualState(
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

        if(javaClass != ExpectedState::class.java && javaClass != ActualState::class.java) {
            return false
        }

        when(other) {
            is ExpectedState -> {
                val progressTolerance = 10000L

                if (!uris.contentEquals(other.uris)) return false
                if (index != other.index) return false
                if (paused != other.paused) return false
                if (progress + progressTolerance < other.progress ||
                    progress - progressTolerance > other.progress) return false
                if (speed != other.speed) return false

                return true
            }
            is ActualState -> {
                val progressTolerance = 10000L

                if (!uris.contentEquals(other.uris)) return false
                if (index != other.index) return false
                if (paused != other.paused) return false
                if (progress + progressTolerance < other.progress ||
                    progress - progressTolerance > other.progress) return false
                if (speed != other.speed) return false

                return true
            }
            else -> return false
        }
    }

    override fun hashCode(): Int {
        var result = uris.contentHashCode()
        result = 31 * result + index
        result = 31 * result + paused.hashCode()
        result = 31 * result + ((progress+5000/10000)*10000).hashCode()
        result = 31 * result + speed.hashCode()

        return result
    }

    fun change(action: (ActualState) -> ExpectedState): ExpectedState {
        return action(this)
    }
}