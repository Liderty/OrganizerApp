package com.liber.organizer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_add_task.*

class AddTaskActivity : AppCompatActivity() {

    val EVALUATE_EVRERYDAY_VALUE = 7

    var context = this
    var db = DataBaseHandler(context)
    var evaluation_flag = false
    var selectedDay = 0
    var selectedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val categoryItem = intent.getSerializableExtra("category") as Category
        val daySpinner = findViewById(R.id.daySpinnerElement) as LinearLayout

        taskImage.setOnClickListener {

        }

        radio_everyday.setOnClickListener {
            this.evaluation_flag = false
            daySpinner.visibility = View.INVISIBLE
        }

        radio_weekly.setOnClickListener {
            this.evaluation_flag = true
            daySpinner.visibility = View.VISIBLE
        }

        val daysSpinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.daysOfWeek)
        )
        daysSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        daysSpinner.adapter = daysSpinnerAdapter

        daysSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedDay = position
            }
        }

        timePicker.setOnTimeChangedListener { _, hour, minute ->
            var hoursInMillis = hour * 60L * 60 * 1000
            var minutesInMillis = minute * 60L * 1000
            selectedTime = hoursInMillis + minutesInMillis
        }


        btnInsert.setOnClickListener {
            if (etvName.text.toString().length > 0 && etvDescription.text.toString().length > 0) {

                if(evaluation_flag) {
                    val task = Task(
                        etvName.text.toString(),
                        etvDescription.text.toString(),
                        selectedDay,
                        selectedTime,
                        categoryItem.categoryId
                    )
                    db.insertTask(task)

                } else {
                    val task = Task(
                        etvName.text.toString(),
                        etvDescription.text.toString(),
                        EVALUATE_EVRERYDAY_VALUE,
                        selectedTime,
                        categoryItem.categoryId
                    )
                    db.insertTask(task)
                }

                finish()
            } else {
                Toast.makeText(context, "Please fill data!", Toast.LENGTH_SHORT).show()
            }
        }

        btnGoBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

//        btnDelete.setOnClickListener {
//            if (etvName.text.toString().length > 0 && etvDescription.text.toString().length > 0) {
//                if (evaluation_flag) {
//                    val task = Task(
//                        etvName.text.toString(),
//                        etvDescription.text.toString(),
//                        selectedDay,
//                        selectedTime,
//                        categoryItem.categoryId
//                    )
//
//                    task.taskUpdateDate = 1546339890000L
//
//                    db.insertTask(task)
//
//                } else {
//
//                    val task = Task(
//                        etvName.text.toString(),
//                        etvDescription.text.toString(),
//                        EVALUATE_EVRERYDAY_VALUE,
//                        selectedTime,
//                        categoryItem.categoryId
//                    )
//
//                    task.taskUpdateDate = 1546339890000L
//
//                    db.insertTask(task)
//                }
//
//            }
//        }

        btnRead.setOnClickListener {
            val data = db.readTasks()
            tvResult.text = ""

            for (i in 0..(data.size - 1)) {
                tvResult.append(
                    "${data.get(i).taskId} | ${data.get(i).taskName} | ${data.get(i).taskDescription} " +
                            "| ${data.get(i).taskAvarage} |date ${data.get(i).taskUpdateDate} |day ${data.get(i).taskEvaluationDay} " +
                            "|time ${data.get(i).taskEvaluationTime} | ${data.get(i).categoryId}\n"
                )
            }
        }
    }
}
