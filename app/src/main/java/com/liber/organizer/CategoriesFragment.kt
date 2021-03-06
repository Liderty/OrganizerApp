package com.liber.organizer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.liber.organizer.NotifyWork.Companion.NOTIFICATION_ID
import kotlinx.android.synthetic.main.fragment_categories.view.*
import kotlin.collections.ArrayList
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.liber.organizer.NotifyWork.Companion.NOTIFICATION_WORK
import androidx.work.ExistingWorkPolicy.REPLACE
import java.util.concurrent.TimeUnit


class CategoriesFragment : Fragment() {

    val EXPIRED_GRADE_EVRYDAY = 7
    val EXPIRED_GRADE_DAY = 14
    val EVALUATE_EVERYDAY = 7

    lateinit var categorylistView: ListView
    lateinit var db: DataBaseHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        categorylistView = view.findViewById(R.id.categoryListView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = getContext()!!
        db = DataBaseHandler(context)

        view.btnCreateCategroy.setOnClickListener {
            val intent = Intent(context, AddCategoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val categoryList = db.readCategory()
        val fm = fragmentManager

        prepareGrades()
        resolveDeprecatedGrades()

        val emptyGrades = db.readEmptyGrades()

        if (db.readTasks().isNotEmpty()) {
            createNotification(TaskDate(getDateForNotification()))
        }

        val ratingsDialog = RatingDialog()
        if (emptyGrades.size > 0) {
            ratingsDialog.show(fm!!, "Ratings_tag")
        }

        categorylistView.adapter =
            CategoryListViewAdapter(context!!, R.layout.listview_category_row, categoryList)

        categorylistView.setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
            val intent = Intent(context, TaskListActivity::class.java)
            intent.putExtra("category", categoryList[position])
            startActivity(intent)
        }
    }

    private fun createMissingGradesForTask(task: Task) {

        val taskEvaluationDay = task.taskEvaluationDay
        val lastUpdateDate = TaskDate(task.taskUpdateDate)
        val currentDate = TaskDate()

        val datesList = currentDate.getDatesListBetweenThisAndOtherDate(lastUpdateDate)
        var daysList = datesList

        if (taskEvaluationDay < EVALUATE_EVERYDAY) {
            daysList = currentDate.getDaysListByIndexFromDatesList(taskEvaluationDay, datesList)
        }

        createGrades(task, daysList)

        val newUpdateDate = TaskDate()
        db.updateTaskUpdateDate(task.taskId, newUpdateDate.milliseconds)
    }

    fun createGrades(task: Task, daysList: ArrayList<Long>) {
        val gradeList = db.readGrades(task.taskId)
        val sortedGrades = gradeList.sortedWith(compareBy({ it.gradeDate }))

        for (day in daysList) {
            if (sortedGrades.isEmpty()) {
                val newGrade = Grade(task.taskId, day)
                db.insertGrade(newGrade)

            } else {
                for (grade in sortedGrades) {
                    if (TaskDate(grade.gradeDate).getDateWithoutTime() == TaskDate(day).getDateWithoutTime()) {
                        break
                    } else if (TaskDate(grade.gradeDate).getDateWithoutTime() > TaskDate(day).getDateWithoutTime()) {
                        val newGrade = Grade(task.taskId, day)
                        db.insertGrade(newGrade)
                        break
                    }
                }
            }
        }
    }

//    fun gradesForEvaluation(): ArrayList<Grade> {
//        val emptyGradeList = db.readEmptyGrades()
//        val gradesForEvaluationList = arrayListOf<Grade>()
//
//        val currentDate = TaskDate()
//
//        for (grade in emptyGradeList) {
//            val gradeTask = db.readTask(grade.taskId)
//            val evaluationFullDateAndTime = TaskDate(grade.gradeDate, gradeTask.taskEvaluationTime)
//
//            if (evaluationFullDateAndTime.milliseconds < currentDate.milliseconds) {
//                gradesForEvaluationList.add(grade)
//            }
//        }
//
//        return gradesForEvaluationList
//    }

    fun resolveDeprecated(grade: Grade, taskList: MutableList<Task>): Boolean {
        val isDeprecated = false

        for (task in taskList) {
            if (grade.taskId == task.taskId) {
                val currentTime = TaskDate()
                val evaluationDate = TaskDate(grade.gradeDate)

                if (task.taskEvaluationDay > 6) {
                    if (currentTime.getDaysBetweenThisAndOtherDate(evaluationDate) > EXPIRED_GRADE_EVRYDAY) {
                        db.updateGradeGrade(grade.gradeId, 1)
                    }
                } else {
                    if (currentTime.getDaysBetweenThisAndOtherDate(evaluationDate) > EXPIRED_GRADE_DAY) {
                        db.updateGradeGrade(grade.gradeId, 1)
                    }
                }
            }
        }

        return isDeprecated
    }

    fun resolveDeprecatedGrades() {
        val gradeList = db.readGrades()
        val taskList = db.readTasks()

        if (gradeList.isNotEmpty()) {
            for (grade in gradeList) {
                if (grade.gradeGrade == 0) {
                    resolveDeprecated(grade, taskList)
                }
            }
        }
    }

    fun prepareGrades() {
        val taskList = db.readTasks()
        if (taskList.size > 0) {
            for (task in taskList) {
                createMissingGradesForTask(task)
            }
        }
    }

    private fun createNotification(notificationTime: TaskDate) {

        val currentTimeDate = TaskDate()

        val customTime = notificationTime.milliseconds
        val currentTime = currentTimeDate.milliseconds

//        println("Current time:      | ${currentTime} |")
//        println("Notification time: | ${customTime} |")

        if (customTime > currentTime) {
            val data = Data.Builder().putInt(NOTIFICATION_ID, 0).build()
            val delay = customTime - currentTime
            scheduleNotification(delay, data)

            Toast.makeText(context, "Notification created", Toast.LENGTH_LONG)
        } else {
            val errorNotificationSchedule = getString(R.string.notification_schedule_error)
            Toast.makeText(context, errorNotificationSchedule, Toast.LENGTH_LONG)
        }

    }

    private fun scheduleNotification(delay: Long, data: Data) {
        val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(context!!)
        instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK, REPLACE, notificationWork).enqueue()
    }

    fun getDateForNotification(): Long {
        val taskList = db.readTasks()
        var currentTime = TaskDate()
        var comparableDate: Long

        var closestDate = currentTime.getNextDayAndTime(
            taskList[0].taskEvaluationDay,
            taskList[0].taskEvaluationTime
        )

        for (i in 0..taskList.size - 1) {
            currentTime = TaskDate()

//            println("Day: ${taskList[i].taskEvaluationDay} Time: ${taskList[i].taskEvaluationTime}")
            comparableDate = currentTime.getNextDayAndTime(
                taskList[i].taskEvaluationDay,
                taskList[i].taskEvaluationTime
            )

//            println("COMPARABLE_DATE: ${comparableDate}")
            if (comparableDate < closestDate) {
                closestDate = comparableDate
            }
        }

        return closestDate
    }


}
