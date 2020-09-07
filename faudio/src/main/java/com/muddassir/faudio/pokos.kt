package com.muddassir.faudio

import java.io.Serializable

data class AudioStateInput(
    val index   : Int,
    val paused  : Boolean,
    val progress: Long,
    val stopped : Boolean
)

data class AudioObservation(
    val error           : String? = null,
    val stopped         : Boolean? = null,
    val paused          : Boolean? = null,
    val index           : Int? = null,
    val progress        : Long? = null,
    val bufferedPosition: Long? = null,
    val duration        : Long? = null
): Serializable