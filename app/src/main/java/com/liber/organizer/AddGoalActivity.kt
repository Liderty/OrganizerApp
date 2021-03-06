package com.liber.organizer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_goal.*

class AddGoalActivity : AppCompatActivity() {

    val context = this

    lateinit var goalTask: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_goal)

        val db = DataBaseHandler(context)
        goalTask = intent.getSerializableExtra("task") as Task

        btnInsert.setOnClickListener {
            if (goalContentInput.text.toString().length > 0) {
                val newGoal = Goal(goalContentInput.text.toString(), goalTask.taskId)
                db.insertGoal(newGoal)
                finish()
            } else {
                Toast.makeText(context, "Please fill data!", Toast.LENGTH_SHORT).show()
            }
        }

        btnGoBack.setOnClickListener {
            finish()
        }
    }
}
