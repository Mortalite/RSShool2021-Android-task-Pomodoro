package com.example.rsshool2021_android_task_pomodoro

import android.graphics.Color
import kotlinx.coroutines.Job

data class Stopwatch(
    val id: Int,
    val durationMs: Long,
    var currentMs: Long,
    var timerJob: Job?,
    var uiJob: Job?,
    var position: Int = 0,
    var color: Int = Color.WHITE,
    var state: STATE = STATE.stop,
) {

    enum class STATE {
        start,
        stop,
        delete,
        complete
    }

}
