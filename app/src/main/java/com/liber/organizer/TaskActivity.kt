package com.liber.organizer

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.activity_task.*


class TaskActivity : AppCompatActivity() {

    lateinit var db: DataBaseHandler

    lateinit var taskImageView: ImageView
    lateinit var taskTitleTextView: TextView
    lateinit var taskDescriptionTextView: TextView
    lateinit var taskGradeTextView: TextView
    lateinit var taskGradeRatingBar: RatingBar

    val EVALUATE_EVERYDAY_STRING = "Everyday"
    val OUT_OF_WEEK = 7
    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val context = this
        db = DataBaseHandler(context)

        val taskItem = intent.getSerializableExtra("task") as Task
        id = taskItem.taskId

        taskImageView = findViewById(R.id.taskImage)
        taskTitleTextView = findViewById(R.id.taskTitle)
        taskDescriptionTextView = findViewById(R.id.taskDescription)
        taskGradeTextView = findViewById(R.id.taskGrade)
        taskGradeRatingBar = findViewById(R.id.taskGradeRatingBar)

        buttonGoals.setOnClickListener {
            val intent = Intent(context, TaskGoalsActivity::class.java)
            intent.putExtra("task", taskItem)
            startActivity(intent)
        }

        btnEdit.setOnClickListener {
            val intent = Intent(context, EditTaskActivity::class.java)
            intent.putExtra("task", taskItem)
            startActivity(intent)
        }

        btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Deleting Task")
            builder.setMessage("WARNING: Are you sure you want to delete this Task? All of its data like grades, goals and statistics will be also deleted!")

            builder.setPositiveButton("Confirm Delete") { dialog, which ->
                if (db.deleteTask(taskItem.taskId)) {
                    db.deleteAllTaskGrades(taskItem.taskId)
                    db.deleteAllTaskGoals(taskItem.taskId)
                    Toast.makeText(applicationContext, "Successfully deleted.", Toast.LENGTH_SHORT)
                        .show()
                }
                finish()
            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                Toast.makeText(applicationContext, "Nothing deleted", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        btnGoBack.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val taskItem = db.readTask(id)

        taskImageView.setImageResource(taskItem.taskIcon)
        taskTitleTextView.text = taskItem.taskName
        taskDescriptionTextView.text = taskItem.taskDescription
        taskGradeTextView.text = taskItem.taskAvarage.toString()

        if (taskItem.taskAvarage != 0.toDouble()) {
            taskGradeRatingBar.rating = taskItem.taskAvarage.toFloat()

        } else {
            taskGradeRatingBar.visibility = View.INVISIBLE
        }
        taskEvaluationDay.text = getDay(taskItem.taskEvaluationDay)
        taskEvaluationTime.text = getTime(taskItem.taskEvaluationTime)


        setUpLineChartData(taskItem.taskId)
    }

    fun getDay(dayId: Int): String {
        if (dayId < OUT_OF_WEEK) {
            return resources.getStringArray(R.array.daysOfWeek)[dayId]
        } else return EVALUATE_EVERYDAY_STRING
    }

    fun getTime(timeInMillis: Long): String {
        val hours = timeInMillis / 1000L / 60 / 60
        val minutes = timeInMillis / 1000L / 60 % 60

        if (minutes == 0L) {
            return "${hours}:00"
        }

        return "${hours}:${minutes}"
    }

    private fun setUpLineChartData(taskId: Int) {
        //Dataset
        val yVals = ArrayList<Entry>()
        val taskGrades = db.readGrades(taskId).reversed()

        for (i in 0..(taskGrades.size - 1)) {
            if(taskGrades[i].gradeGrade != 0) {
                yVals.add(Entry(i.toFloat(), taskGrades[i].gradeGrade.toFloat(), i.toString()))
            }
        }

        val set1: LineDataSet
        set1 = LineDataSet(yVals, "All ${db.readTask(taskId).taskName} grades")

        //Chart colors
        set1.color = Color.BLUE
        set1.setCircleColor(Color.BLUE)

        //Line
        set1.lineWidth = 1f

        //Points settings
        set1.circleRadius = 5f
        set1.setDrawCircleHole(false)
        set1.valueTextSize = 1f
        set1.setDrawFilled(false)

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)
        val data = LineData(dataSets)

        //Chart options
        taskGradesLineChart.setData(data)
        taskGradesLineChart.description.isEnabled = false
//        taskGradesLineChart.legend.isEnabled = false
        taskGradesLineChart.setPinchZoom(true)
        taskGradesLineChart.legend.textSize = 15f
        taskGradesLineChart.legend.formSize = 15f
//        taskGradesLineChart.xAxis.enableGridDashedLine(5f, 5f, 0f)
//        taskGradesLineChart.axisRight.enableGridDashedLine(5f, 5f, 0f)
//        taskGradesLineChart.axisLeft.enableGridDashedLine(5f, 5f, 0f)
        taskGradesLineChart.setDrawGridBackground(false)
        taskGradesLineChart.xAxis.labelCount = 14
        taskGradesLineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
    }
}
