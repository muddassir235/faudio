package com.muddassir.faudio

class AudioPill(var audio: Audio) {
    /**
     * Change the audio through an action (e.g. start, stop, pause, e.t.c)
     *
     * @param action The action to perform on the Audio
     */
    fun act(action: (Audio)->Audio) {
        this.audio = action.invoke(audio)
    }
}
