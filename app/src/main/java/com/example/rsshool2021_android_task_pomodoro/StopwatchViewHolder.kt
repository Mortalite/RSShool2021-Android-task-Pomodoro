package com.example.rsshool2021_android_task_pomodoro

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import android.util.Log
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.rsshool2021_android_task_pomodoro.databinding.StopwatchItemBinding
import kotlin.concurrent.thread

class StopwatchViewHolder(
    private val binding: StopwatchItemBinding,
    private val listener: StopwatchListener,
    private val stopwatchViewModel: StopwatchViewModel,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        stopwatchViewModel.setValues(listener)
    }

    fun bind(stopwatch: Stopwatch) {
        binding.stopwatchTimer.text = stopwatchViewModel.displayTime(stopwatch.currentMs)
        stopwatch.binding = binding

        initButtonsListeners(stopwatch)
    }

    private fun initButtonsListeners(stopwatch: Stopwatch) {
        binding.startPauseButton.setOnClickListener {
            if (stopwatch.isStarted) {
                listener.startForm(stopwatch)
                listener.stop(stopwatch.id, stopwatch.currentMs)
                stopwatchViewModel.stopTimer(stopwatch)
            }
            else if (stopwatch.currentMs > 0) {
//                listener.stopForm(stopwatch)
                listener.start(stopwatch.id)
                stopwatchViewModel.startTimer(stopwatch)
            }
        }

        binding.restartButton.setOnClickListener {
            listener.startForm(stopwatch)
            listener.reset(stopwatch.id, stopwatch.initMs)
            stopwatchViewModel.stopTimer(stopwatch)
        }
        binding.deleteButton.setOnClickListener {
            listener.startForm(stopwatch)
            listener.delete(stopwatch.id)
        }
    }


}