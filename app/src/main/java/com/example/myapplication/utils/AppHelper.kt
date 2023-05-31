package com.example.myapplication.utils

import android.graphics.Paint
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

object AppHelper {

    const val timeDateFormat = "hh:mm a"
    const val hour24Format = "HH"
    const val hourFormat = "hh"
    const val minuteFormat = "mm"
    const val amPmFormat = "a"

    inline var TextView.strike: Boolean
        set(visible) {
            paintFlags = if (visible) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        get() = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG == Paint.STRIKE_THRU_TEXT_FLAG

    fun getDateFromMilliseconds(millis: Long, dateFormat: String): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return formatter.format(calendar.time).toUpperCase()
    }
}