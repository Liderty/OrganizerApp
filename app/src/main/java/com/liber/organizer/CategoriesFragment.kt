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
import kotlin.collections.ArrayList

class CategoriesFragment : Fragment() {

    lateinit var categorylistView: ListView
    lateinit var db: DataBaseHandler

    fun createMissingGradesForTask(task: Task) {

        var taskEvaluationDay = task.taskEvaluationDay
        var lastUpdateDate = TaskDate(task.taskUpdateDate)
        var currentDate = TaskDate()

        var datesList = currentDate.getDatesListBetweenThisAndOtherDate(lastUpdateDate)
        var daysList = currentDate.getDaysListByIndexFromDatesList(taskEvaluationDay, datesList)

        createGrades(task, daysList)
    }

    fun createGrades(task: Task, daysList: ArrayList<Long>) {
        var gradeList = db.readGrades()

        var sortedGrades = gradeList.sortedWith(compareBy({it.gradeDate}))

        for (day in daysList) {
            for( grade in sortedGrades ) {
                if(TaskDate(grade.gradeDate).getDateWithoutTime() == TaskDate(day).getDateWithoutTime()) {
                    break
                } else if(TaskDate(grade.gradeDate).getDateWithoutTime() > TaskDate(day).getDateWithoutTime()) {
                    val newGrade = Grade(task.taskId, day)
                    db.insertGrade(newGrade)
                    break
                }
            }
        }
    }

    fun popupGradesForEvaluation() {
        TODO("Not implemented") // check list of grades that are missing grade and show popup with list of them
    }

    fun resolveOldGrades () {
        TODO("Not implemented") // automatically evaluate grades older than seted
    }

    fun evaluateGrades() {
        val taskList = db.readTasks()

        if(taskList.size > 0) {
            for (i in 0..(taskList.size - 1)) {
                createMissingGradesForTask(taskList.get(i))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        categorylistView = view.findViewById(R.id.categoryListView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = getContext()
        db = DataBaseHandler(context!!)

        // TESTBLOCK.START

        evaluateGrades()

        // TESTBLOCK.END

        var categoryList = db.readCategory()

        categorylistView.adapter = CategoryListViewAdapter(context, R.layout.listview_category_row, categoryList)
        categorylistView.setOnItemClickListener{ parent: AdapterView<*>, view: View, position: Int, id: Long ->

            var intent = Intent(context, TaskListActivity::class.java)
            intent.putExtra("category", categoryList[position])
            startActivity(intent)
        }

        view.btnCreateCategroy.setOnClickListener {
            var intent = Intent(context, AddCategoryActivity::class.java)
            startActivity(intent)
        }

        view.openGrades.setOnClickListener() {
            var intent = Intent(context, AddGradeActivity::class.java)
            startActivity(intent)
        }
    }
}
