package com.liber.organizer

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.activity_task.btnGoBack


class TaskActivity : AppCompatActivity() {

    lateinit var db: DataBaseHandler
    lateinit var taskImageView: ImageView
    lateinit var taskTitleTextView: TextView
    lateinit var taskDescriptionTextView: TextView
    lateinit var taskGradeTextView: TextView

    val EVALUATE_EVERYDAY_STRING = "Everyday"
    val OUT_OF_WEEK = 7

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val context = this
        db = DataBaseHandler(context)

        val taskItem = intent.getSerializableExtra("task") as Task

        taskImageView = findViewById(R.id.taskImage)
        taskTitleTextView = findViewById(R.id.taskTitle)
        taskDescriptionTextView = findViewById(R.id.taskDescription)
        taskGradeTextView = findViewById(R.id.taskGrade)

        btnEdit.setOnClickListener {
            var intent = Intent(context, EditTaskActivity::class.java)
            intent.putExtra("task", taskItem)
            startActivity(intent)
        }

        btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Deleting Task")
            builder.setMessage("WARNING: Are you sure you want to delete this Task? All of its data like grades and statistics will be also deleted!")

            builder.setPositiveButton("Confirm Delete"){dialog, which ->
                if(db.deleteTask(taskItem.taskId)) {
                    db.deleteAllTaskGrades(taskItem.taskId)
                    Toast.makeText(applicationContext,"Successfully deleted.",Toast.LENGTH_SHORT).show()
                }
                finish()
            }

            builder.setNegativeButton("Cancel"){dialog,which ->
                Toast.makeText(applicationContext,"Nothing deleted",Toast.LENGTH_SHORT).show()
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
        val taskItem = db.readTask(taskId)

        taskImageView.setImageResource(taskItem.taskIcon)
        taskTitleTextView.text = taskItem.taskName
        taskDescriptionTextView.text = taskItem.taskDescription
        taskGradeTextView.text = taskItem.taskAvarage.toString()
        taskEvaluationDay.text = getDay(taskItem.taskEvaluationDay)
        taskEvaluationTime.text = getTime(taskItem.taskEvaluationTime)

        setUpPieChartData(taskItem.taskId)
    }

    fun getDay(dayId: Int) : String {
        if(dayId < OUT_OF_WEEK) {
            return resources.getStringArray(R.array.daysOfWeek)[dayId]
        } else return EVALUATE_EVERYDAY_STRING
    }

    fun getTime(timeInMillis: Long) : String {
        val hours = timeInMillis / 1000L / 60 / 60
        val minutes = timeInMillis / 1000L / 60 % 60

        if(minutes == 0L) {
            return "${hours}:00"
        }

        return "${hours}:${minutes}"
    }

    private fun setUpPieChartData(taskId: Int) {
        val yVals = ArrayList<Entry>()
        val taskGrades = db.readGrades(taskId)

        for(i in 0..(taskGrades.size - 1)) {
            yVals.add(Entry(taskGrades[i].gradeGrade.toFloat(), i.toFloat() , i.toString()))
        }

        yVals.add(Entry(11f, 28f, "11"))

        val set1: LineDataSet
        set1 = LineDataSet(yVals, "DataSet 1")

        set1.color = Color.BLUE
        set1.setCircleColor(Color.BLUE)
        set1.lineWidth = 1f
        set1.circleRadius = 3f
        set1.setDrawCircleHole(false)
        set1.valueTextSize = 0f
        set1.setDrawFilled(false)

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)
        val data = LineData(dataSets)

        // set data
        taskGradesLineChart.setData(data)
        taskGradesLineChart.description.isEnabled = false
        taskGradesLineChart.legend.isEnabled = false
        taskGradesLineChart.setPinchZoom(true)
        taskGradesLineChart.xAxis.enableGridDashedLine(5f, 5f, 0f)
        taskGradesLineChart.axisRight.enableGridDashedLine(5f, 5f, 0f)
        taskGradesLineChart.axisLeft.enableGridDashedLine(5f, 5f, 0f)
        //lineChart.setDrawGridBackground()
        taskGradesLineChart.xAxis.labelCount = 11
        taskGradesLineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
    }
}
