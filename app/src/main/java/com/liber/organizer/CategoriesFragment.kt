package com.liber.organizer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import kotlinx.android.synthetic.main.fragment_categories.view.*

class CategoriesFragment : Fragment() {

    lateinit var tasksListView: ListView
    lateinit var db: DataBaseHandler

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        tasksListView = view.findViewById(R.id.tasksListView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = getContext()
        db = DataBaseHandler(context!!)

        updateAvarage()
        var tasksList = db.readTasks()

        tasksListView.adapter = TaskListViewAdapter(context, R.layout.listview_task_row, tasksList)
        tasksListView.setOnItemClickListener{ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            var intent = Intent(context, TaskActivity::class.java)
            intent.putExtra("task", tasksList[position])
            startActivity(intent)
        }

        view.btnCreateTask.setOnClickListener {
            var intent = Intent(context, AddTaskActivity::class.java)
            startActivity(intent)
        }

        view.btnGoToDBGradeTab.setOnClickListener {
            var intent = Intent(context, AddGradeActivity::class.java)
            startActivity(intent)
        }

    }
}

