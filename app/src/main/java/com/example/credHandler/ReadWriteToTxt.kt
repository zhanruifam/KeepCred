package com.example.credHandler

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

const val fileName = "credHandlerV1.txt"
const val fTimeOut = "credHandlerTimeOutV1.txt"

fun storeTimeOutData(context: PasswordActivity, fileName: String, timeOutHour: Int, timeOutMinute: Int) {
    val file = File(context.filesDir, fileName)
    try {
        val writer = BufferedWriter(FileWriter(file, true))
        val data = "$timeOutHour|$timeOutMinute"
        writer.write(data)
        writer.flush()
        writer.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun readTimeOutData(context: Context, fileName: String): Pair<Int, Int> {
    val file = File(context.filesDir, fileName)
    var timeOutHour = 0; var timeOutMinute = 0
    try {
        val reader = BufferedReader(FileReader(file))
        var line: String? = reader.readLine()
        while(line != null) {
            val tmp1: String = line.substringBefore("|", getCurrentTime().first.toString())
            val tmp2: String = line.substringAfter("|", getCurrentTime().second.toString())
            timeOutHour = tmp1.toInt()
            timeOutMinute = tmp2.toInt()
            line = reader.readLine()
        }
        reader.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return Pair(timeOutHour, timeOutMinute)
}

fun storeDataToFile(context: Context, fileName: String, data: String) {
    val file = File(context.filesDir, fileName)
    try {
        val writer = BufferedWriter(FileWriter(file, true))
        writer.write(data)
        writer.flush()
        writer.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun instantiateTxtFile(context: Context, fileName: String) {
    val file = File(context.filesDir, fileName)
    try {
        val reader = BufferedReader(FileReader(file))
        var line: String? = reader.readLine()
        var iter = 0 // for task id
        while(line != null) {
            val tmp1: String = line.substringBefore("|", "empty title")
            val tmp2: String = line.substringAfter("|", "empty description")
            TaskLib.trackObject(AddTask(iter++, tmp1, tmp2))
            line = reader.readLine()
        }
        reader.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun clearOrReplace(context: Context, fileName: String, taskID: Int, rFlag: Boolean) {
    val file = File(context.filesDir, fileName)
    try {
        if(file.exists()) {
            val lines = mutableListOf<String>()
            val reader = BufferedReader(FileReader(file))
            var line: String? = reader.readLine()
            while(line != null) {
                lines.add(line)
                line = reader.readLine()
            }
            reader.close()
            lines.removeAt(taskID)
            if(rFlag) { // only execute if changes are made
                lines.add(taskID, TaskLib.getAnyWithin(taskID).getFormattedString())
            }
            val writer = BufferedWriter(FileWriter(file, false))
            for (txtline in lines) {
                writer.write(txtline)
                writer.newLine()
            }
            writer.close()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun prioritizeLine(context: Context, fileName: String, taskID: Int) {
    val file = File(context.filesDir, fileName)
    try {
        if(file.exists()) {
            val lines = mutableListOf<String>()
            val reader = BufferedReader(FileReader(file))
            var line: String? = reader.readLine()
            while(line != null) {
                lines.add(line)
                line = reader.readLine()
            }
            reader.close()
            val shifted = mutableListOf<String>()
            shifted.add(lines[taskID])
            for(txtline in lines) {
                if (txtline != lines[taskID]) {
                    shifted.add(txtline)
                }
            }
            val writer = BufferedWriter(FileWriter(file, false))
            for(txtline in shifted) {
                writer.write(txtline)
                writer.newLine()
            }
            writer.close()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun clearTextFile(context: Context, fileName: String) {
    val file = File(context.filesDir, fileName)
    try {
        if(file.exists()) {
            val fileWriter = FileWriter(file, false)
            fileWriter.close()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun isTextFileNotEmpty(context: Context, fileName: String): Boolean {
    val file = File(context.filesDir, fileName)
    if (!file.exists() || !file.isFile) {
        return false
    }
    val content = file.readText()
    return content.isNotEmpty()
}