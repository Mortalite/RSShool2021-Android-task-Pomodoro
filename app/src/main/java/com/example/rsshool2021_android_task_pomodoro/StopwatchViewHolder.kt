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
