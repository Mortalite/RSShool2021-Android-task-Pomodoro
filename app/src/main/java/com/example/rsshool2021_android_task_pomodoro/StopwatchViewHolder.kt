package com.example.rsshool2021_android_task_pomodoro

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.util.Log
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.rsshool2021_android_task_pomodoro.databinding.StopwatchItemBinding
import kotlinx.coroutines.*

class StopwatchViewHolder(
    private val binding: StopwatchItemBinding,
    private val listener: StopwatchListener,
    private val stopwatchViewModel: StopwatchViewModel
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(stopwatch: Stopwatch) {
        stopwatchViewModel.listener = listener
        stopwatchViewModel.initStopwatchItem(binding, stopwatch)
        stopwatchViewModel.initButtonsListeners(binding, stopwatch)
    }


    companion object {

        const val START_TIME = "00:00:00"
        const val UNIT_TEN_MS = 10L

    }

}

/*


package com.example.rsshool2021_android_task_pomodoro

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.util.Log
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.rsshool2021_android_task_pomodoro.databinding.StopwatchItemBinding
import kotlinx.coroutines.*

class StopwatchViewHolder(
    private val binding: StopwatchItemBinding,
    private val listener: StopwatchListener,
    private val stopwatchViewModel: StopwatchViewModel
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(stopwatch: Stopwatch) {
        binding.stopwatchTimer.text = displayTime(stopwatch.currentMs)
        binding.root.setCardBackgroundColor(stopwatch.color)
        binding.circleView.setPeriod(stopwatch.durationMs)
        binding.circleView.setCurrent(stopwatch.durationMs - stopwatch.currentMs)

        if (stopwatch.state == Stopwatch.STATE.start) {
            stopwatch.uiJob?.cancel()
            stopwatch.uiJob = startUpdateUI(stopwatch = stopwatch)
        }
        if (stopwatch.state == Stopwatch.STATE.stop)
            stopUpdateUI(stopwatch)

        initButtonsListeners(stopwatch)
    }

    private fun initButtonsListeners(stopwatch: Stopwatch) {
        binding.startPauseButton.setOnClickListener {
            if (stopwatch.state == Stopwatch.STATE.start)
                changeState(Stopwatch.STATE.stop, stopwatch)
            else if (stopwatch.state == Stopwatch.STATE.stop && stopwatch.currentMs > 0)
                changeState(Stopwatch.STATE.start, stopwatch)
        }

        binding.deleteButton.setOnClickListener {
            changeState(Stopwatch.STATE.delete, stopwatch)
        }
    }


    fun changeState(state: Stopwatch.STATE, stopwatch: Stopwatch) {
        stopwatch.state = state
        if (state == Stopwatch.STATE.start)
            start(stopwatch)
        if (state == Stopwatch.STATE.delete)
            delete(stopwatch)
    }

    private fun start(stopwatch: Stopwatch) {
        stopRunningStopwatch(stopwatch)
        stopwatch.uiJob?.cancel()
        stopwatch.uiJob = startUpdateUI(stopwatch = stopwatch)
        stopwatch.timerJob = startTimer(stopwatch = stopwatch)
    }

    private fun stopRunningStopwatch(stopwatch: Stopwatch) {
        stopwatchViewModel.stopwatches
            .forEach {
                if (it.id != stopwatch.id)
                    it.state = Stopwatch.STATE.stop
            }
    }

    private fun delete(stopwatch: Stopwatch) {
        stopwatchViewModel.stopwatches.remove(stopwatchViewModel.stopwatches.find { it.id == stopwatch.id })
        listener.submitNewList(stopwatchViewModel.stopwatches.toMutableList())
    }

    private fun startAnimation(stopwatchItemBinding: StopwatchItemBinding) {
        stopwatchItemBinding.blinkingIndicator.apply {
            isInvisible = false
            (background as? AnimationDrawable)?.start()
        }
    }

    private fun stopAnimation(stopwatchItemBinding: StopwatchItemBinding) {
        stopwatchItemBinding.blinkingIndicator.apply {
            isInvisible = true
            (background as? AnimationDrawable)?.stop()
        }
    }

    private fun startUpdateUI(delayMillis: Long = UNIT_TEN_MS, stopwatch: Stopwatch) = MainScope().launch(Dispatchers.Main) {
        stopwatch.apply {
            startAnimation(binding)
            binding.startPauseButton.text = "STOP"
            while (stopwatch.state == Stopwatch.STATE.start) {
                delay(delayMillis)
                binding.circleView.setCurrent(durationMs - currentMs)
                binding.stopwatchTimer.text = displayTime(stopwatch.currentMs)
            }
            stopUpdateUI(stopwatch)
        }
    }

    private fun stopUpdateUI(stopwatch: Stopwatch) = MainScope().launch(Dispatchers.Main) {
        stopwatch.apply {
            binding.startPauseButton.text = "START"
            stopAnimation(binding)
            Log.e("UI STOP", "CANCEL")
        }
    }

    private fun startTimer(delayMillis: Long = UNIT_TEN_MS, stopwatch: Stopwatch) = MainScope().launch(Dispatchers.IO) {
        stopwatch.apply {
            while (stopwatch.state == Stopwatch.STATE.start && stopwatch.currentMs > 0) {
                delay(delayMillis)
                currentMs -= delayMillis
            }
            MainScope().launch(Dispatchers.Main) {
                if (currentMs <= 0L) {
                    stopwatch.color = Color.RED
                    stopwatch.state = Stopwatch.STATE.complete
                    listener.notifyDataSetChanged()
                }
                Log.e("STOP TIMER", "CANCEL")
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

    private fun displaySlot(count: Long): String = when {
        count / 10L > 0 -> "$count"
        else -> "0$count"
    }

    companion object {

        const val START_TIME = "00:00:00"
        const val UNIT_TEN_MS = 10L

    }

}

 */