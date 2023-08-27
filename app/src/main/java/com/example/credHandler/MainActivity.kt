package com.example.credHandler

import android.os.Bundle
import java.io.IOException
import android.widget.Button
import android.content.Intent
import android.widget.EditText
import android.app.AlertDialog
import android.widget.TextView
import android.widget.LinearLayout
import android.annotation.SuppressLint
import androidx.cardview.widget.CardView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private lateinit var totalTask: TextView
    private lateinit var txt1: TextView
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var done: Button
    private lateinit var inputTxt1: EditText
    private lateinit var inputTxt2: EditText
    private lateinit var cardLists: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainactivity)

        btn1 = findViewById(R.id.btn1)
        btn2 = findViewById(R.id.btn2)
        btn3 = findViewById(R.id.btn3)
        inputTxt1 = findViewById(R.id.inputText1)
        inputTxt2 = findViewById(R.id.inputText2)
        cardLists = findViewById(R.id.cardContainer)

        if(!PasswordActivity.enteredOnce && isTextFileNotEmpty(this, fileName)) {
            PasswordActivity.enteredOnce = true
            instantiateTxtFile(this, fileName)
        }
        refreshView()

        btn1.setOnClickListener { // add task
            if(inputTxt1.text.toString().isNotEmpty()) {
                val title = inputTxt1.text.toString().replace("\n", ", ")
                val descr = inputTxt2.text.toString().replace("\n", ", ")
                TaskLib.trackObject(AddTask(TaskLib.getTotalTasks(), title, descr))
                storeDataToFile(this, fileName, TaskLib.getRecent().getFormattedString() + "\n")
            }
            refreshView()
        }

        btn2.setOnClickListener { // delete all task(s) and reset
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmation")
                .setMessage("Remove " + TaskLib.getTotalTasks().toString() + " cred(s)?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, _ ->
                    clearTextFile(this, fileName)
                    removeEveryCardViews()
                    TaskLib.delAllObject()
                    displayTotalTask()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            builder.create().show()
        }

        btn3.setOnClickListener { // refresh view
            refreshView()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun refreshView() {
        displayTotalTask()
        removeEveryCardViews()
        showCardViews()
        inputTxt1.setText("")
        inputTxt2.setText("Username:  Password: ")
    }

    private fun showCardViews() {
        for(obj in TaskLib.getAllObjects()) {
            val card = layoutInflater.inflate(R.layout.card_view_layout, CardView(this))
            card.tag = obj.getTaskID()
            card.setOnClickListener {
                val intent = Intent(this, OpenCardView::class.java)
                intent.putExtra("ID", obj.getTaskID())
                intent.putExtra("TITLE", obj.getTaskTitle())
                intent.putExtra("DESC", obj.getTaskDesc())
                startActivity(intent)
            }
            done = card.findViewById(R.id.taskDone)
            done.setOnClickListener { // remove specific card view
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirmation")
                    .setMessage("Remove " + obj.getDspString() + "?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, _ ->
                        clearOrReplace(this, fileName, obj.getTaskID(), false)
                        TaskLib.delThatObject(obj.getTaskID())
                        refreshView()
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                builder.create().show()
            }
            txt1 = card.findViewById(R.id.info_text)
            txt1.text = obj.getDspString()
            cardLists = findViewById(R.id.cardContainer)
            cardLists.addView(card)
        }
    }

    private fun removeCardView(cardTag: Any?) {
        cardLists = findViewById(R.id.cardContainer)
        val card = cardLists.findViewWithTag(cardTag) as? CardView
        try {
            cardLists.removeView(card)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun removeEveryCardViews() {
        for(i in 0 .. TaskLib.getTotalTasks()) {
            // take note that removeCardView() takes in parameter of Any type
            // passing in Int type to Any type still works
            removeCardView(i)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayTotalTask() {
        totalTask = findViewById(R.id.totalTask)
        totalTask.text = TaskLib.getTotalTasks().toString() + " existing cred(s)"
    }
}