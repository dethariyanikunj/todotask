package com.example.myapplication.addtask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityAddTaskBinding
import com.example.myapplication.db.DbUtils

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnCancel.setOnClickListener {
            finish()
        }
        binding.btnAdd.setOnClickListener {
            val data = binding.outlinedTextField.editText!!.text.toString().trim()
            if (data.isNotEmpty()) {
                DbUtils.saveTasks(
                    taskTime = System.currentTimeMillis(),
                    taskTitle = data
                )
                finish()
            }
        }
    }
}