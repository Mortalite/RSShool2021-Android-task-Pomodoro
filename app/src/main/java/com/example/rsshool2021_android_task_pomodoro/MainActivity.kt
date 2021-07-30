package com.example.rsshool2021_android_task_pomodoro

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rsshool2021_android_task_pomodoro.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity :    AppCompatActivity(),
                        StopwatchListener,
                        LifecycleObserver {

    private lateinit var binding: ActivityMainBinding
    private lateinit var stopwatchViewModel: StopwatchViewModel
    private lateinit var stopwatchAdapter: StopwatchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        initViewModel()
        initStopwatchAdapter()
        initRecyclerView()
        setOnClickButtonListener()
    }

    fun initViewModel() {
        stopwatchViewModel = ViewModelProvider(this).get(StopwatchViewModel::class.java)
    }

    fun initStopwatchAdapter() {
        stopwatchAdapter = StopwatchAdapter(this, stopwatchViewModel)
    }

    fun initRecyclerView() {
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = stopwatchAdapter
        }
    }

    fun setOnClickButtonListener() {
        binding.addNewStopwatchButton.setOnClickListener {

            val text: String? = binding.addNewEditText.text?.toString()

            if (!text.isNullOrBlank()) {
                val matches = Regex("""^(\d+):?(\d*)$""").findAll(text)
                val matchesIt = matches.iterator()

                if (matchesIt.hasNext()) {
                    val matchResult = matchesIt.next()
                    val minutes = matchResult.groupValues[1].toLongOrNull()
                    val seconds = matchResult.groupValues[2].toLongOrNull() ?: 0

                    if (minutes != null) {
                        val durationMs  = (minutes * 60 + seconds) * 1000

                        if (durationMs > 0) {
                            stopwatchViewModel.let {
                                it.stopwatches.add(
                                    Stopwatch(
                                        it.nextId++, durationMs, durationMs,
                                        null, null, Color.WHITE
                                    )
                                )
                                submitNewList(it.stopwatches.toMutableList())
                            }
                        }
                    }
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
         val runningStopwatch = stopwatchViewModel
            .stopwatches
            .find { it.state == Stopwatch.STATE.start }

        runningStopwatch?.let {
            val startIntent = Intent(this, ForegroundService::class.java)
            startIntent.putExtra(COMMAND_ID, COMMAND_START)
            startIntent.putExtra(STARTED_TIMER_TIME_MS, it.currentMs)
            startService(startIntent)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        val stopIntent = Intent(this, ForegroundService::class.java)
        stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        startService(stopIntent)
    }


    override fun submitNewList(newList: MutableList<Stopwatch>) {
        stopwatchAdapter.submitList(newList)

    }

    override fun notifyDataSetChanged() {
        stopwatchAdapter.notifyDataSetChanged()
    }


}