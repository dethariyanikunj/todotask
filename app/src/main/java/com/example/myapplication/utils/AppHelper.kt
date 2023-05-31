package com.example.myapplication.utils

import java.text.SimpleDateFormat
import java.util.*

object AppHelper {

    fun getDateFromMilliseconds(millis: Long): String {
        val dateFormat = "hh:mm a"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return formatter.format(calendar.time).toUpperCase()
    }
}