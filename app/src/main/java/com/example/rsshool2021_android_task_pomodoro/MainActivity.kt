package com.example.rsshool2021_android_task_pomodoro

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rsshool2021_android_task_pomodoro.databinding.ActivityMainBinding
import com.example.rsshool2021_android_task_pomodoro.databinding.StopwatchItemBinding

class MainActivity :    AppCompatActivity(),
                        StopwatchListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var stopwatchViewModel: StopwatchViewModel
    private lateinit var stopwatchAdapter: StopwatchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                        val startMs  = (minutes * 60 + seconds) * 1000
                        stopwatchViewModel.let {
                            it.stopwatches.add(Stopwatch(it.nextId++, startMs, startMs, false, null, null))
                            stopwatchAdapter.submitList(it.stopwatches.toList())
                        }
                    }
                }
            }
        }
    }

    override fun start(id: Int) {
        changeStopwatch(id, null, true)
    }

    override fun stop(id: Int, currentMs: Long) {
        changeStopwatch(id, currentMs, false)
    }

    override fun reset(id: Int, startMs: Long) {
        changeStopwatch(id, startMs, false)
    }

    override fun delete(id: Int) {
        stopwatchViewModel.let {
            it.stopwatches.remove(getStopwatchById(id))
            stopwatchAdapter.submitList(it.stopwatches.toList())
        }
    }

    override fun setColor(stopwatchItemBinding: StopwatchItemBinding, color: Int) {
        stopwatchItemBinding.root.setCardBackgroundColor(color)
//        stopwatchItemBinding.startPauseButton.setBackgroundColor(color)
//        stopwatchItemBinding.restartButton.setBackgroundColor(color)
//        stopwatchItemBinding.deleteButton.setBackgroundColor(color)
    }

    override fun startForm(stopwatch: Stopwatch) {
        stopwatch.binding?.let { setColor(it, Color.WHITE) }
        stopwatch.binding?.startPauseButton?.text = "START"
    }

    override fun stopForm(stopwatch: Stopwatch) {
        stopwatch.binding?.startPauseButton?.text = "STOP"
    }

    override fun completeForm(stopwatch: Stopwatch) {
        stopwatch.binding?.let { setColor(it, Color.RED) }
        stopwatch.binding?.startPauseButton?.text = "START"
    }

    private fun getStopwatchById(id: Int): Stopwatch? {
        return stopwatchViewModel.stopwatches.find { it.id == id }
    }

    private fun changeStopwatch(id: Int, currentMs: Long?, isStarted: Boolean) {
        stopwatchViewModel.let {
            it.stopwatches
                .find {
                    it.id == id
                }
                ?.let {
                    it.currentMs = currentMs ?: it.currentMs
                    it.isStarted = isStarted
                }
            stopwatchAdapter.notifyDataSetChanged()
        }
    }
}