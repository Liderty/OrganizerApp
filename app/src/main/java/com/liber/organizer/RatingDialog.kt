package com.liber.organizer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_rating_list.*

class RatingDialog : DialogFragment() {

    lateinit var emptyGrades: List<Grade>
    lateinit var db: DataBaseHandler

    lateinit var taskImageView: ImageView
    lateinit var taskTitleTextView: TextView
    lateinit var taskDescriptionTextView: TextView
    lateinit var taskDate: TextView
    lateinit var taskGradeRatingBar: RatingBar
    lateinit var taskRatingBar: RatingBar
    lateinit var taskRateButton: Button
    lateinit var taskAvarageView: LinearLayout

    lateinit var goalsListView: ListView

    var epmtyGradesNumber = 0
    var gradeIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        db = DataBaseHandler(this.context!!)

        emptyGrades = db.readEmptyGrades().reversed()
        this.epmtyGradesNumber = emptyGrades.size

        val rootView = inflater.inflate(R.layout.dialog_rating_list, container)

        goalsListView = rootView.findViewById(R.id.goalListview) as ListView

        taskImageView = rootView.findViewById(R.id.taskImage)
        taskTitleTextView = rootView.findViewById(R.id.taskTitle)
        taskDescriptionTextView = rootView.findViewById(R.id.taskDescription)
        taskDate = rootView.findViewById(R.id.taskDate)
        taskGradeRatingBar = rootView.findViewById(R.id.taskGrade)
        taskRatingBar = rootView.findViewById(R.id.taskRatingBar)
        taskRateButton = rootView.findViewById(R.id.btnRate)
        taskAvarageView = rootView.findViewById(R.id.taskAvarageView)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData(emptyGrades[gradeIndex])

        btnCloseRating.setOnClickListener {
            this.dismiss()
        }
    }

    fun setData(gradeItem: Grade) {
        val taskItem = db.readTask(gradeItem.taskId)
        val taskItemDate = TaskDate(gradeItem.gradeDate)

        setNumber(gradeIndex)

        taskImageView.setImageDrawable(this.context!!.resources.getDrawable(taskItem.taskIcon))
        taskTitleTextView.text = taskItem.taskName
        taskDescriptionTextView.text = taskItem.taskDescription
        taskDate.text = taskItemDate.getStringDate()

        if (taskItem.taskAvarage != 0.toDouble()) {
            println("TAKS AVARAGE: ${taskItem.taskAvarage.toFloat()}")
            taskGradeRatingBar.rating = taskItem.taskAvarage.toFloat()
            taskAvarageView.visibility = View.VISIBLE
        } else {
            taskAvarageView.visibility = View.INVISIBLE
        }

        val goalsList = db.readGoalsInProgess(taskItem.taskId)
        goalsListView.adapter =
            RatingListViewAdapter(this.context!!, R.layout.listview_rating_row, goalsList)

        taskRateButton.setOnClickListener {
            db.updateGradeGrade(gradeItem.gradeId, taskRatingBar.rating.toInt())
            gradeIndex++
            updateAvarage()
            if (gradeIndex != epmtyGradesNumber) {
                setData(emptyGrades[gradeIndex])
            } else {
                this.dismiss()
            }
        }
    }

    private fun countAvarage(gradeList: List<Int>): Double {
        return Math.round((gradeList.sum().toDouble() / gradeList.size) * 10.0) / 10.0
    }

    private fun getTaskIds(): ArrayList<Int> {
        val taskList = db.readTasks()
        val taskIdList = arrayListOf<Int>()

        for (i in 0..(taskList.size - 1)) {
            taskIdList.add(taskList.get(i).taskId)
        }

        return taskIdList
    }

    private fun getGradesForTask(taskId: Int) : ArrayList<Int> {
        val gradesList = db.readGrades()
        val taskGradesList = ArrayList<Int>()

        for (grade in gradesList) {
            if (taskId == grade.taskId && grade.gradeGrade != 0) {
                taskGradesList.add(grade.gradeGrade)
            }
        }
        return taskGradesList
    }

    private fun updateAvarage() {
        val taskIdList = getTaskIds()

        for (taskId in taskIdList) {
            val taskGradesList = getGradesForTask(taskId)

            if (taskGradesList.isNotEmpty()) {
                val newTaskAvarage = countAvarage(taskGradesList)
                db.updateTaskAvarage(taskId, newTaskAvarage)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun setNumber(gradeIndex: Int) {
        graduateProgress.text = "${gradeIndex + 1}/${epmtyGradesNumber}"
    }
}