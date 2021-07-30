package com.example.rsshool2021_android_task_pomodoro

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.util.Log
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModel
import com.example.rsshool2021_android_task_pomodoro.databinding.StopwatchItemBinding
import kotlinx.coroutines.*

class StopwatchViewModel: ViewModel() {

    val stopwatches = mutableListOf<Stopwatch>()
    var listener: StopwatchListener? = null
    var nextId = 0

    fun initStopwatchItem(
        binding: StopwatchItemBinding,
        stopwatch: Stopwatch) {
            binding.stopwatchTimer.text = displayTime(stopwatch.currentMs)
            binding.root.setCardBackgroundColor(stopwatch.color)
            binding.circleView.setPeriod(stopwatch.durationMs)
            binding.circleView.setCurrent(stopwatch.durationMs - stopwatch.currentMs)

            if (stopwatch.state == Stopwatch.STATE.start) {
                stopwatch.uiJob?.cancel()
                stopwatch.uiJob = startUpdateUI(binding, stopwatch)
            }
            if (stopwatch.state == Stopwatch.STATE.stop)
                stopUpdateUI(binding)
    }

    fun initButtonsListeners(
        binding: StopwatchItemBinding,
        stopwatch: Stopwatch) {
        binding.startPauseButton.setOnClickListener {
            if (stopwatch.state == Stopwatch.STATE.start)
                changeState(Stopwatch.STATE.stop, binding, stopwatch)
            else if (stopwatch.state == Stopwatch.STATE.stop && stopwatch.currentMs > 0)
                changeState(Stopwatch.STATE.start, binding, stopwatch)
        }

        binding.deleteButton.setOnClickListener {
            changeState(Stopwatch.STATE.delete, binding, stopwatch)
        }
    }

    fun changeState(
        state: Stopwatch.STATE,
        binding: StopwatchItemBinding,
        stopwatch: Stopwatch) {
        stopwatch.state = state
        if (state == Stopwatch.STATE.start)
            start(binding, stopwatch)
        if (state == Stopwatch.STATE.delete)
            delete(stopwatch)
    }

    private fun start(
        binding: StopwatchItemBinding,
        stopwatch: Stopwatch) {
        stopRunningStopwatch(stopwatch)
        stopwatch.apply {
            uiJob?.cancel()
            uiJob = startUpdateUI(binding, stopwatch)
            timerJob = startTimer(stopwatch)
        }
    }

    private fun stopRunningStopwatch(stopwatch: Stopwatch) {
        stopwatches
            .forEach {
                if (it.id != stopwatch.id)
                    it.state = Stopwatch.STATE.stop
            }
    }

    private fun delete(stopwatch: Stopwatch) {
        stopwatches.remove(stopwatches.find { it.id == stopwatch.id })
        listener?.submitNewList(stopwatches.toMutableList())
    }

    private fun startAnimation(binding: StopwatchItemBinding) {
        binding.blinkingIndicator.apply {
            isInvisible = false
            (background as? AnimationDrawable)?.start()
        }
    }

    private fun stopAnimation(binding: StopwatchItemBinding) {
        binding.blinkingIndicator.apply {
            isInvisible = true
            (background as? AnimationDrawable)?.stop()
        }
    }

    private fun startUpdateUI(
        binding: StopwatchItemBinding,
        stopwatch: Stopwatch,
        delayMillis: Long = StopwatchViewHolder.UNIT_TEN_MS) = MainScope().launch(Dispatchers.Main) {
        startAnimation(binding)
        binding.startPauseButton.text = "STOP"
        while (stopwatch.state == Stopwatch.STATE.start) {
            delay(delayMillis)
            binding.circleView.setCurrent(stopwatch.durationMs - stopwatch.currentMs)
            binding.stopwatchTimer.text = displayTime(stopwatch.currentMs)
        }
        stopUpdateUI(binding)
    }

    private fun stopUpdateUI(binding: StopwatchItemBinding) = MainScope().launch(Dispatchers.Main) {
        binding.apply {
            startPauseButton.text = "START"
            stopAnimation(binding)
        }
    }

    private fun startTimer(stopwatch: Stopwatch, delayMillis: Long = StopwatchViewHolder.UNIT_TEN_MS) = MainScope().launch(Dispatchers.IO) {
        stopwatch.apply {
            val startMs = System.currentTimeMillis()
            val currentDurationMs = currentMs

            while (stopwatch.state == Stopwatch.STATE.start && stopwatch.currentMs > 0) {
                delay(delayMillis)
                currentMs = (currentDurationMs + startMs - System.currentTimeMillis())
            }
            MainScope().launch(Dispatchers.Main) {
                if (currentMs <= 0L) {
                    stopwatch.color = Color.RED
                    stopwatch.state = Stopwatch.STATE.complete
                    listener?.notifyDataSetChanged()
                }
            }
        }
    }

}