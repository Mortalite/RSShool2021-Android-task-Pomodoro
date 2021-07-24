package com.example.rsshool2021_android_task_pomodoro

import android.graphics.Color
import com.example.rsshool2021_android_task_pomodoro.databinding.StopwatchItemBinding
import kotlinx.coroutines.Job

data class Stopwatch(
    val id: Int,
    val initMs: Long,
    var currentMs: Long,
    var isStarted: Boolean,
    var timer: Job?,
    var binding: StopwatchItemBinding?,

)
