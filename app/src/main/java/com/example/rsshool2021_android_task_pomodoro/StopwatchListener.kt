package com.example.rsshool2021_android_task_pomodoro

interface StopwatchListener {

    fun submitNewList(newList: MutableList<Stopwatch>)
    fun notifyDataSetChanged()

}
