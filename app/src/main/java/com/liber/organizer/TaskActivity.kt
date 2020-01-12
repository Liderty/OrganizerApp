package com.liber.organizer

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.liber.organizer.MainActivity
import com.liber.organizer.R
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.activity_task.btnGoBack
import java.time.LocalDateTime
import java.util.*

class TaskActivity : AppCompatActivity() {

    val EVALUATE_EVERYDAY_STRING = "Everyday"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        var context = this
        var db = DataBaseHandler(context)

        val taskItem = intent.getSerializableExtra("task") as Task

        val taskImageView: ImageView = findViewById(R.id.taskImage)
        val taskTitleTextView: TextView = findViewById(R.id.taskTitle)
        val taskDescriptionTextView: TextView = findViewById(R.id.taskDescription)
        val taskGradeTextView: TextView = findViewById(R.id.taskGrade)

        taskImageView.setImageResource(R.drawable.settings)
        taskTitleTextView.text = taskItem.taskName
        taskDescriptionTextView.text = taskItem.taskDescription
        taskGradeTextView.text = taskItem.taskAvarage.toString()
        taskEvaluationDay.text = getDay(taskItem.taskEvaluationDay)
        taskEvaluationTime.text = getTime(taskItem.taskEvaluationTime)

        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.plus, R.drawable.check)
            .addSubMenu(Color.parseColor("#48E84D"), R.drawable.digit_five)
            .addSubMenu(Color.parseColor("#C3E848"), R.drawable.digit_four)
            .addSubMenu(Color.parseColor("#FFE45C"), R.drawable.digit_three)
            .addSubMenu(Color.parseColor("#E8A848"), R.drawable.digit_two)
            .addSubMenu(Color.parseColor("#FF662E"), R.drawable.digit_one)
            .setOnMenuSelectedListener {
                index ->
                    var newGrade = Grade(6-(index+1), taskItem.taskId)
                    db.insertGrade(newGrade)
                recreate()
            }


        btnGoBack.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun getDay(dayId: Int) : String {
        if(dayId < 8) {
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
}
