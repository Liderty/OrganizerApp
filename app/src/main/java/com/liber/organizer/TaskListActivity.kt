package com.liber.organizer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView

class TaskListActivity : AppCompatActivity() {

    lateinit var tasksListView: ListView

    var context = this
    var db = DataBaseHandler(context)


    fun countAvarage(gradeList: List<Int>): Double {
        return Math.round((gradeList.sum().toDouble() / gradeList.size) * 10.0) / 10.0
    }

    fun updateAvarage() {
        val taskIdList = arrayListOf<Int>()

        var taskList = db.readTasks()
        var gradeList = db.readGrades()

        for (i in 0..(taskList.size - 1)) {
            taskIdList.add(taskList.get(i).taskId)
        }

        for (i in 0..(taskIdList.size - 1)) {
            var gradeListForSpecificTask: MutableList<Int> = ArrayList()

            for (k in 0..(gradeList.size - 1)) {
                if(taskIdList[i] == gradeList[k].taskId){
                    gradeListForSpecificTask.add(gradeList[k].gradeGrade)
                }
            }

            if(gradeListForSpecificTask.isNotEmpty()) {
                var newTaskAvarage = countAvarage(gradeListForSpecificTask)
                db.updateTaskAvarage(taskIdList[i], newTaskAvarage)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        val categoryItem = intent.getSerializableExtra("category") as Category

        var btnCreateTask = findViewById<LinearLayout>(R.id.btnCreateTask)

        updateAvarage()

        var tasksList = db.readTasks(categoryItem.categoryId)

        tasksListView = findViewById(R.id.tasksListView)
        tasksListView.adapter = TaskListViewAdapter(this, R.layout.listview_task_row, tasksList)

        tasksListView.setOnItemClickListener{ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            var intent = Intent(this, TaskActivity::class.java)
            intent.putExtra("task", tasksList[position])
            startActivity(intent)
        }

        btnCreateTask.setOnClickListener {
            var intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra("category", categoryItem)
            startActivity(intent)
        }
    }
}
