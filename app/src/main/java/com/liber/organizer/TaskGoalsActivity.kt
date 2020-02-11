package com.liber.organizer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_task_goals.*

class TaskGoalsActivity : AppCompatActivity() {

    lateinit var db: DataBaseHandler

    lateinit var goalsListView: ListView
    lateinit var goalTask: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_goals)

        val context = this
        db = DataBaseHandler(context)

        goalsListView = findViewById(R.id.goalListview)

        goalTask = intent.getSerializableExtra("task") as Task

        val goalsList = db.readGoals(goalTask.taskId)
        goalsListView.adapter = TaskGoalsListViewAdapter(this, R.layout.listview_rating_row, goalsList)

        buttonAddGoal.setOnClickListener {
            var intent = Intent(context, AddGoalActivity::class.java)
            intent.putExtra("task", goalTask)
            startActivity(intent)
        }

        buttonReturn.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        val goalsList = db.readGoals(goalTask.taskId)
        goalsListView.adapter = TaskGoalsListViewAdapter(this, R.layout.listview_rating_row, goalsList)
    }
}
