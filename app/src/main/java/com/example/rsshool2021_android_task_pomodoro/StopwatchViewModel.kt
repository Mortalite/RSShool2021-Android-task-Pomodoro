package com.example.rsshool2021_android_task_pomodoro

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.util.Log
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class StopwatchViewModel: ViewModel() {

    val stopwatches = mutableListOf<Stopwatch>()
    var nextId = 0
    var runningStopwatchId = -1

    private var listener: StopwatchListener? = null

    fun setValues(listener: StopwatchListener) {
        this.listener = listener
    }

    fun startTimer(stopwatch: Stopwatch) {
        stopwatches.forEach {
            if (it.id == runningStopwatchId) {
                Log.e("VM", "${it.id}, ${runningStopwatchId}")
                listener?.startForm(it)
                it.isStarted = false
                stopTimer(it)
            }
        }
        listener?.stopForm(stopwatch)
        stopwatch.timer = startCoroutineTimer(stopwatch = stopwatch)
        stopwatch.timer?.start()
        runningStopwatchId = stopwatch.id
        startAnimation(stopwatch)
    }

    fun stopTimer(stopwatch: Stopwatch) {
        stopwatch.timer?.cancel()
        stopAnimation(stopwatch)
    }

    private fun startAnimation(stopwatch: Stopwatch) {
        stopwatch.binding?.blinkingIndicator?.apply {
            isInvisible = false
            (background as? AnimationDrawable)?.start()
        }
    }

    private fun stopAnimation(stopwatch: Stopwatch) {
        stopwatch.binding?.blinkingIndicator?.apply {
            isInvisible = true
            (background as? AnimationDrawable)?.stop()
        }
    }

    private fun startCoroutineTimer(delayMillis: Long = UNIT_TEN_MS, stopwatch: Stopwatch) = GlobalScope.launch(Dispatchers.IO) {
        stopwatch.apply {
            while (currentMs > 0) {
                delay(delayMillis)
                currentMs -= delayMillis
                GlobalScope.launch(Dispatchers.Main) {
                    binding?.stopwatchTimer?.text = displayTime(currentMs)
                }
            }
            GlobalScope.launch(Dispatchers.Main) {
                if (currentMs <= 0L) {
                    listener?.completeForm(stopwatch)
//                    binding?.let { listener?.setColor(it, Color.RED) }
                    stopAnimation(stopwatch)
                }
            }
        }
    }

     fun displayTime(timestamp: Long): String {
        if (timestamp <= 0L)
            return START_TIME

        val h = timestamp / 1000 / 3600
        val m = timestamp / 1000 % 3600 / 60
        val s = timestamp / 1000 % 60
//        val ms = timestamp % 1000 / 10

//         return "${displaySlot(h)}:${displaySlot(m)}:${displaySlot(s)}:${displaySlot(ms)}"
         return "${displaySlot(h)}:${displaySlot(m)}:${displaySlot(s)}"
    }

    private fun displaySlot(count: Long): String {
        return if (count / 10L > 0) {
            "$count"
        } else {
            "0$count"
        }
    }

    private companion object {

        private const val START_TIME = "00:00:00"
        private const val UNIT_TEN_MS = 10L

    }

}