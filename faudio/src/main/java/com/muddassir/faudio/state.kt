package com.muddassir.faudio

import android.net.Uri

data class ActualAudioItem(
    val uri: Uri,
    val download: Boolean,
    val downloadPaused: Boolean,
    val downloadProgress: Float
) {
    override fun equals(other: Any?): Boolean {
        return this.uri == (other as? ExpectedAudioItem)?.uri
                || this.uri == (other as? ActualAudioItem)?.uri
    }

    override fun hashCode(): Int {
        return uri.hashCode()
    }
}

data class ExpectedAudioItem(
    val uri: Uri,
    val download: Boolean
) {
    override fun equals(other: Any?): Boolean {
        return this.uri == (other as? ExpectedAudioItem)?.uri
                || this.uri == (other as? ActualAudioItem)?.uri
    }

    override fun hashCode(): Int {
        return uri.hashCode()
    }
}

data class ActualAudioState(
    val items                : List<ActualAudioItem>,
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

        if(javaClass != ExpectedAudioState::class.java && javaClass != ActualAudioState::class.java) {
            return false
        }

        when(other) {
            is ExpectedAudioState -> {
                val progressTolerance = 10000L

                if (items != other.items) return false
                if (index != other.index) return false
                if (paused != other.paused) return false
                if (progress + progressTolerance < other.progress ||
                    progress - progressTolerance > other.progress) return false
                if (speed != other.speed) return false

                return true
            }
            is ActualAudioState -> {
                val progressTolerance = 10000L

                if (items != other.items) return false
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
        var result = items.hashCode()
        result = 31 * result + index
        result = 31 * result + paused.hashCode()
        result = 31 * result + ((((progress+10000)/20000)+1.0).toInt()*20000).hashCode()
        result = 31 * result + speed.hashCode()

        return result
    }

    fun change(action: (ActualAudioState) -> ExpectedAudioState): ExpectedAudioState {
        return action(this)
    }
}

data class ExpectedAudioState(
    val items: List<ExpectedAudioItem>,
    val index: Int,
    val paused: Boolean,
    val progress: Long,
    val speed: Float,
    val stopped: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if(javaClass != ExpectedAudioState::class.java && javaClass != ActualAudioState::class.java) {
            return false
        }

        when(other) {
            is ExpectedAudioState -> {
                val progressTolerance = 10000L

                if (items != other.items) return false
                if (index != other.index) return false
                if (paused != other.paused) return false
                if (progress + progressTolerance < other.progress ||
                    progress - progressTolerance > other.progress) return false
                if (speed != other.speed) return false

                return true
            }
            is ActualAudioState -> {
                val progressTolerance = 10000L

                if (items != other.items) return false
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
        var result = items.hashCode()
        result = 31 * result + index
        result = 31 * result + paused.hashCode()
        result = 31 * result + ((((progress+10000)/20000)+1.0).toInt()*20000).hashCode()
        result = 31 * result + speed.hashCode()

        return result
    }

    companion object {
        val DEFAULT = ExpectedAudioState(
            emptyList(), 0, true, 0, 1.0f, true)

        fun defaultStateWithUris(uris: List<Uri>): ExpectedAudioState {
            return ExpectedAudioState(
                uris.map { ExpectedAudioItem(it, false) },
                DEFAULT.index,
                DEFAULT.paused,
                DEFAULT.progress,
                DEFAULT.speed,
                DEFAULT.stopped
            )
        }
    }
}