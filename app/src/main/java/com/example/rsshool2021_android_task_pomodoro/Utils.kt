package com.example.rsshool2021_android_task_pomodoro

const val INVALID = "INVALID"
const val COMMAND_START = "COMMAND_START"
const val COMMAND_STOP = "COMMAND_STOP"
const val COMMAND_ID = "COMMAND_ID"
const val STARTED_TIMER_TIME_MS = "STARTED_TIMER_TIME"

fun displayTime(timestamp: Long): String {
    if (timestamp <= 0L)
        return StopwatchViewHolder.START_TIME

    val h = timestamp / 1000 / 3600
    val m = timestamp / 1000 % 3600 / 60
    val s = timestamp / 1000 % 60
//        val ms = timestamp % 1000 / 10

//         return "${displaySlot(h)}:${displaySlot(m)}:${displaySlot(s)}:${displaySlot(ms)}"
    return "${displaySlot(h)}:${displaySlot(m)}:${displaySlot(s)}"
}

private fun displaySlot(count: Long): String = when {
    count / 10L > 0 -> "$count"
    else -> "0$count"
}