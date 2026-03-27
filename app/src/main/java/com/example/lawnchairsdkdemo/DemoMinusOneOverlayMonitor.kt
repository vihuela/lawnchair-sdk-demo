package com.example.lawnchairsdkdemo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.lawnchair.sdk.api.MinusOneOverlayState

object DemoMinusOneOverlayMonitor {
    var state by mutableStateOf(MinusOneOverlayState.Closed)
        private set

    var progress by mutableFloatStateOf(0f)
        private set

    fun update(state: MinusOneOverlayState, progress: Float) {
        this.state = state
        this.progress = progress
    }
}
