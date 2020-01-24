package com.liber.organizer

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.activity_task.btnGoBack


class TaskActivity : AppCompatActivity() {

    val EVALUATE_EVERYDAY_STRING = "Everyday"
    val OUT_OF_WEEK = 7

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val context = this
        val db = DataBaseHandler(context)

        val taskItem = intent.getSerializableExtra("task") as Task

        val taskImageView: ImageView = findViewById(R.id.taskImage)
        val taskTitleTextView: TextView = findViewById(R.id.taskTitle)
        val taskDescriptionTextView: TextView = findViewById(R.id.taskDescription)
        val taskGradeTextView: TextView = findViewById(R.id.taskGrade)

        taskImageView.setImageResource(R.drawable.settings)
        taskTitleTextView.text = taskItem.taskName
        taskDescriptionTextView.text = taskItem.taskDescription
        taskGradeTextView.text = taskItem.taskAvarage.toString()
        taskEvaluationDay.text = getDay(taskItem.taskEvaluationDay)
        taskEvaluationTime.text = getTime(taskItem.taskEvaluationTime)

        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.plus, R.drawable.check)
            .addSubMenu(Color.parseColor("#48E84D"), R.drawable.digit_five)
            .addSubMenu(Color.parseColor("#C3E848"), R.drawable.digit_four)
            .addSubMenu(Color.parseColor("#FFE45C"), R.drawable.digit_three)
            .addSubMenu(Color.parseColor("#E8A848"), R.drawable.digit_two)
            .addSubMenu(Color.parseColor("#FF662E"), R.drawable.digit_one)
            .setOnMenuSelectedListener {
                index ->
                    val newGrade = Grade(6-(index+1), taskItem.taskId)
                    db.insertGrade(newGrade)
                recreate()
            }

        btnEdit.setOnClickListener {
            var intent = Intent(context, EditTaskActivity::class.java)
            intent.putExtra("task", taskItem)
            startActivity(intent)
        }

        btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Deleting Task")
            builder.setMessage("WARNING: Are you sure you want to delete this Task? All of its data like grades and statistics will be also deleted!")

            builder.setPositiveButton("Confirm Delete"){dialog, which ->
                if(db.deleteTask(taskItem.taskId)) {
                    db.deleteAllTaskGrades(taskItem.taskId)
                    Toast.makeText(applicationContext,"Successfully deleted.",Toast.LENGTH_SHORT).show()
                }
                finish()
            }

            builder.setNegativeButton("Cancel"){dialog,which ->
                Toast.makeText(applicationContext,"Nothing deleted",Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        btnGoBack.setOnClickListener {
            finish()
        }
    }

    fun getDay(dayId: Int) : String {
        if(dayId < OUT_OF_WEEK) {
            return resources.getStringArray(R.array.daysOfWeek)[dayId]
        } else return EVALUATE_EVERYDAY_STRING
    }

    fun getTime(timeInMillis: Long) : String {
        val hours = timeInMillis / 1000L / 60 / 60
        val minutes = timeInMillis / 1000L / 60 % 60

        if(minutes == 0L) {
            return "${hours}:00"
        }

        return "${hours}:${minutes}"
    }
}
