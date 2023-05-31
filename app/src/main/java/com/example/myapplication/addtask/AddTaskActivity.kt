package com.example.myapplication.addtask

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityAddTaskBinding
import com.example.myapplication.db.DbUtils
import com.example.myapplication.utils.AppHelper
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*


class AddTaskActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityAddTaskBinding
    private var currentHour: Int = 0
    private var currentMinute: Int = 0
    private var selectedHour: Int = 0
    private var selectedMinute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        handleClickEvents()
    }

    private fun init() {
        val timeCategories = ArrayList<String>()
        timeCategories.add(resources.getString(R.string.timeInAm))
        timeCategories.add(resources.getString(R.string.timeInPm))
        val timeAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeCategories)
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spTime.adapter = timeAdapter
        setCurrentTime()
    }

    private fun setCurrentTime() {
        val currentTime = System.currentTimeMillis()
        currentHour =
            AppHelper.getDateFromMilliseconds(currentTime, AppHelper.hourFormat)
                .toInt()
        currentMinute =
            AppHelper.getDateFromMilliseconds(currentTime, AppHelper.minuteFormat)
                .toInt()
        val amPm =
            AppHelper.getDateFromMilliseconds(currentTime, AppHelper.amPmFormat)
        if (amPm.equals(resources.getString(R.string.timeInAm), ignoreCase = true)) {
            binding.spTime.setSelection(0)
        } else {
            binding.spTime.setSelection(1)
        }
        binding.tlTaskTime.editText!!.text =
            Editable.Factory.getInstance()
                .newEditable(String.format("%02d:%02d", currentHour, currentMinute))
        selectedHour = currentHour
        selectedMinute = currentMinute
    }

    private fun handleClickEvents() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnCancel.setOnClickListener {
            finish()
        }
        binding.btnAdd.setOnClickListener {
            Log.d("Tag", "$selectedHour $selectedMinute")
            val task = binding.tlTaskTitle.editText!!.text.toString().trim()
            val calendar = Calendar.getInstance()
            calendar.set(
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DATE],
            )
            calendar.set(Calendar.HOUR, selectedHour)
            calendar.set(Calendar.MINUTE, selectedMinute)
            if (binding.spTime.selectedItemPosition == 0) {
                calendar.set(Calendar.AM_PM, Calendar.AM)
            } else {
                calendar.set(Calendar.AM_PM, Calendar.PM)
            }

            if (task.isNotEmpty()) {
                DbUtils.saveTasks(
                    taskTime = calendar.timeInMillis,
                    taskTitle = task
                )
                finish()
            }
        }
        binding.llTimeClick.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(currentHour)
                    .setMinute(currentMinute)
                    .setTitleText(resources.getString(R.string.task_time))
                    .build()
            picker.addOnPositiveButtonClickListener {
                binding.spTime.setSelection(0)
                var hour = 0
                if (picker.hour == 0) {
                    hour = 12
                } else if (picker.hour >= 13) {
                    hour = picker.hour - 12
                    binding.spTime.setSelection(1)
                } else {
                    hour = picker.hour
                }
                binding.tlTaskTime.editText!!.text =
                    Editable.Factory.getInstance()
                        .newEditable(String.format("%02d:%02d", hour, picker.minute))
                selectedHour = hour
                selectedMinute = picker.minute
            }
            picker.show(this.supportFragmentManager, "time_picker")
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("Tag", "$position")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Nothing to do.
    }
}