package com.example.credHandler

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import android.widget.Button
import android.content.Intent
import android.widget.EditText
import android.app.AlertDialog
import android.annotation.SuppressLint
import androidx.annotation.RequiresApi
import androidx.activity.ComponentActivity
import android.text.method.PasswordTransformationMethod

const val maxTries: Int     = 3
const val password: String  = "348934"

class PasswordActivity : ComponentActivity() {
    companion object {
        var enteredOnce: Boolean = false
        var numberTries = 0
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.password)

        if(isTextFileNotEmpty(this, fTimeOut)) {
            timeOutCheck()
        }

        val passwordButton: Button = findViewById(R.id.enterpw)
        val passwordInput: EditText = findViewById(R.id.passwordfield)
        passwordInput.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        passwordInput.transformationMethod = PasswordTransformationMethod.getInstance()

        passwordButton.setOnClickListener {
            ++numberTries
            if(passwordInput.text.toString() == password) {
                numberTries = 0
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                maxTriesCheck()
                passwordInput.setText("")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun maxTriesCheck() {
        if(numberTries == maxTries) {
            val (timeOutHour, timeOutMinute) = calculateTimeOut()
            storeTimeOutData(this, fTimeOut, timeOutHour, timeOutMinute)
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Suspected Breach")
                .setMessage("Maximum tries reached, try again later")
                .setCancelable(false)
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }
            builder.create().show()
        } else {
            Toast.makeText(this, "Invalid passcode", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun timeOutCheck() {
        val (cH, cM) = getCurrentTime()
        val (tOH, tOM) = readTimeOutData(this, fTimeOut)
        if((tOH == cH && tOM > cM) || ((tOH == cH+1 || (tOH == 0 && cH == 23)) && cM > tOM)) {
            // TODO: include year, month, and day?
            val reTOMin = remainingTimeOut(cM, tOM)
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Suspected Breach")
                .setMessage("Timeout alert, try again in $reTOMin minute(s)")
                .setCancelable(false)
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }
            builder.create().show()
        } else {
            clearTextFile(this, fTimeOut)
        }
    }
}