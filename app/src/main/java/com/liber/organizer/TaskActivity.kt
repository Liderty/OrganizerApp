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

class TaskActivity : AppCompatActivity() {

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
}
