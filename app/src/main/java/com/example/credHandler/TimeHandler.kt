package com.example.credHandler

import android.os.Build
import java.time.LocalDateTime
import androidx.annotation.RequiresApi

const val BUFFER_PERIOD: Int    = 15 // timeout period
const val MINUTES_IN_HOUR: Int  = 60
const val FINAL_HOUR: Int       = 23

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentTime(): Pair<Int, Int> {
    val currentDateTime = LocalDateTime.now()
    val hour = currentDateTime.hour
    val minute = currentDateTime.minute
    return Pair(hour, minute)
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateTimeOut(): Pair<Int, Int> {
    val timeOutHour: Int
    val timeOutMinute: Int
    val (hour, minute) = getCurrentTime()
    if(minute + BUFFER_PERIOD >= MINUTES_IN_HOUR) {
        timeOutMinute = (minute + BUFFER_PERIOD) - MINUTES_IN_HOUR
        timeOutHour = if(hour == FINAL_HOUR) {
            0
        } else {
            hour + 1
        }
    } else {
        timeOutMinute = minute + BUFFER_PERIOD
        timeOutHour = hour
    }
    return Pair(timeOutHour, timeOutMinute)
}

fun remainingTimeOut(currMinute: Int, timeOutMinute: Int): Int {
    val remainder: Int = if(timeOutMinute >= currMinute) {
        timeOutMinute - currMinute
    } else {
        (MINUTES_IN_HOUR - currMinute) + timeOutMinute
    }
    return remainder
}