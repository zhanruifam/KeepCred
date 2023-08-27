package com.example.credHandler

import android.os.Bundle
import android.widget.Toast
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity

class OpenCardView : ComponentActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.open_card_view)

        val taskOrCardID: Int = intent.getIntExtra("ID", 0)
        val taskOrCardTitle: String? = intent.getStringExtra("TITLE")?.replace(", ", "\n")
        val taskOrCardDesc: String? = intent.getStringExtra("DESC")?.replace(", ", "\n")

        val iDText: TextView = findViewById(R.id.cardtaskid)
        val titleText: TextView = findViewById(R.id.cardtasktitle)
        val descText: TextView = findViewById(R.id.cardtaskdesc)
        val finBtn: Button = findViewById(R.id.finishTask)
        val editBtn: Button = findViewById(R.id.edit)
        val priorBtn: Button = findViewById(R.id.priority)
        val titleEdit: EditText = findViewById(R.id.titleEdit)
        val descEdit: EditText = findViewById(R.id.descEdit)

        iDText.text = "ID: $taskOrCardID"
        titleText.text = "Title: \n$taskOrCardTitle"
        descText.text = "Description: \n$taskOrCardDesc"
        titleEdit.setText(taskOrCardTitle)
        descEdit.setText(taskOrCardDesc)

        editBtn.setOnClickListener { // change title and description
            if(titleEdit.text.toString().isNotEmpty()) {
                val title = titleEdit.text.toString().replace("\n", ", ")
                val descr = descEdit.text.toString().replace("\n", ", ")
                TaskLib.getAnyWithin(taskOrCardID).setTaskTitle(title)
                TaskLib.getAnyWithin(taskOrCardID).setTaskDesc(descr)
                clearOrReplace(this, fileName, taskOrCardID, true)
                Toast.makeText(this, "Changed, please refresh", Toast.LENGTH_SHORT).show()
                finish() // return back to main page
            } else {
                Toast.makeText(this, "Title to be filled", Toast.LENGTH_SHORT).show()
            }
        }

        priorBtn.setOnClickListener { // change priority
            if(taskOrCardID != 0) {
                TaskLib.prioritizeItem(taskOrCardID)
                prioritizeLine(this, fileName, taskOrCardID)
                Toast.makeText(this, "Please refresh", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Invalid request", Toast.LENGTH_SHORT).show()
            }
            finish() // return back to main page
        }

        finBtn.setOnClickListener { // return with no changes
            finish() // return back to main page
        }
    }
}