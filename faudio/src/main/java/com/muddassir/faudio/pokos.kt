package com.muddassir.faudio

data class AudioStateInput(
    val index   : Int,
    val paused  : Boolean,
    val progress: Long,
    val stopped : Boolean
)

data class AudioObservation(
    val error           : String?,
    val stopped         : Boolean?,
    val paused          : Boolean?,
    val index           : Int?,
    val progress        : Long?,
    val bufferedPosition: Long?,
    val duration        : Long?
)
