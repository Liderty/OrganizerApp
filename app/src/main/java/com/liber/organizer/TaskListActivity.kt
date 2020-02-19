package com.liber.organizer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_task_list.*

class TaskListActivity : AppCompatActivity() {

    lateinit var tasksListView: ListView

    var categoryId = 0
    var context = this
    var db = DataBaseHandler(context)


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        val categoryItem = intent.getSerializableExtra("category") as Category

        categoryImage.setImageResource(categoryItem.categoryIcon)
        categoryName.setText(categoryItem.categoryName)

        categoryId = categoryItem.categoryId
        var btnCreateTask = findViewById<LinearLayout>(R.id.btnCreateTask)

        tasksListView = findViewById(R.id.tasksListView)

        btnCreateTask.setOnClickListener {
            var intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra("category", categoryItem)
            startActivity(intent)
        }

        buttonEditCategory.setOnClickListener {
            var intent = Intent(context, EditCategoryActivity::class.java)
            intent.putExtra("category", categoryItem)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val tasksList = db.readTasks(categoryId)
        val categoryItem = db.readCategory(categoryId)

        categoryImage.setImageResource(categoryItem.categoryIcon)
        categoryName.setText(categoryItem.categoryName)

        tasksListView.adapter = TaskListViewAdapter(this, R.layout.listview_task_row, tasksList)
        tasksListView.setOnItemClickListener{ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            val intent = Intent(this, TaskActivity::class.java)
            intent.putExtra("task", tasksList[position])
            startActivity(intent)
        }
    }
}
