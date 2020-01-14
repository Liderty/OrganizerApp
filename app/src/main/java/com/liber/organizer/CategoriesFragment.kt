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
import java.util.*

class CategoriesFragment : Fragment() {

    lateinit var categorylistView: ListView
    lateinit var db: DataBaseHandler

    fun getListOfDatesForTaskGrades(task: Task) {
        var dateList = arrayListOf<Long>()

        var currentDate = Calendar.getInstance()



        TODO("Not implemented") // returns list of dates for grades
    }

    fun createGrades(taskId: Int, dateList: Long) {
        TODO("Not implemented") // create missing grades
    }

    fun popupGradesForEvaluation() {
        TODO("Not implemented") // check list of grades that are missing grade and show popup with list of them
    }

    fun resolveOldGrades () {
        TODO("Not implemented") // automatically evaluate grades older than seted
    }

    fun evaluateGrades() {
        var taskList = db.readTasks()
        var gradeList = db.readGrades()

        for (i in 0..(taskList.size - 1)) {
            getListOfDatesForTaskGrades(taskList.get(i))
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
    }
}

