package com.example.myapplication.tasks

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.addtask.AddTaskActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.db.DbUtils
import com.example.myapplication.model.TaskInfo
import com.google.android.material.datepicker.MaterialDatePicker
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.UpdatedResults
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        handleClickEvents()
    }

    private fun init() {
        val list = DbUtils.retrieveTasks()
        watchOnTaskChanges(list)
        updateList(list)
    }

    private fun handleClickEvents() {
        binding.fab.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddTaskActivity::class.java))
        }
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.ivMore.setOnClickListener {
            val popup = PopupMenu(this@MainActivity, binding.ivMore)
            popup.menuInflater.inflate(R.menu.poupup_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.filterByDate) {
                    showDatePicker()
                }
                true
            }
            popup.show()
        }
    }

    private fun showDatePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(resources.getString(R.string.select_date))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        datePicker.show(this.supportFragmentManager, "date_picker")
        datePicker.addOnPositiveButtonClickListener {
            val startTime = it
            val endTime = it + (1439 * 60000) // 1 day = (1440 minutes - 1) * 60000 => millisecond
            filterByDate(startTime, endTime)
        }
    }

    private fun updateList(list: RealmResults<TaskInfo>, isFilteredApplied: Boolean = false) {
        binding.tvNoTask.visibility = View.GONE
        if (list.isEmpty()) {
            binding.rvTask.visibility = View.GONE
            if (isFilteredApplied) {
                binding.tvNoTask.visibility = View.VISIBLE
            }
        } else {
            binding.rvTask.visibility = View.VISIBLE
            binding.rvTask.adapter = TaskItemAdapter(this@MainActivity, list)
        }
    }

    private fun filterByDate(startTime: Long, endTime: Long) {
        val list = DbUtils.retrieveDateWiseTasks(startTime, endTime)
        updateList(list, isFilteredApplied = true)
    }

    private fun watchOnTaskChanges(list: RealmResults<TaskInfo>) {
        job = CoroutineScope(Dispatchers.Default).launch {
            val itemsFlow = list.asFlow()
            itemsFlow.collect { changes: ResultsChange<TaskInfo> ->
                when (changes) {
                    is UpdatedResults -> {
                        CoroutineScope(Dispatchers.Main).launch {
                            updateList(changes.list)
                        }
                    }
                    else -> {
                        // Nothing to Do.
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        if (::job.isInitialized) {
            job.cancel()
        }
        super.onDestroy()
    }
}