package com.example.rsshool2021_android_task_pomodoro

import com.example.rsshool2021_android_task_pomodoro.databinding.StopwatchItemBinding

interface StopwatchListener {

    fun start(id: Int)
    fun stop(id: Int, currentMs: Long)
    fun reset(id: Int, startMs: Long)
    fun delete(id: Int)
    fun setColor(stopwatchItemBinding: StopwatchItemBinding, color: Int)
    fun startForm(stopwatch: Stopwatch)
    fun stopForm(stopwatch: Stopwatch)
    fun completeForm(stopwatch: Stopwatch)
}
