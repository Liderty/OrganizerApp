package com.liber.organizer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_add_task.*
import java.util.*

class AddTaskActivity : AppCompatActivity() {
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

                Toast.makeText(
                    context,
                    "Spinner selected : ${position}",
                    Toast.LENGTH_SHORT
                ).show()

//                parent?.getItemAtPosition(position).toString()
            }
        }


        timePicker.setOnTimeChangedListener { _, hour, minute ->
            var calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)

            selectedTime = calendar.timeInMillis
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
                        selectedDay,
                        selectedTime,
                        categoryItem.categoryId
                    )
                    db.insertTask(task)
                }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(context, "Please fill data!", Toast.LENGTH_SHORT).show()
            }
        }

        btnGoBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnRead.setOnClickListener {
            val data = db.readTasks()
            tvResult.text = ""

            for (i in 0..(data.size - 1)) {
                tvResult.append(
                    "${data.get(i).taskId} | ${data.get(i).taskName} | ${data.get(i).taskDescription} " +
                            "| ${data.get(i).taskAvarage} | ${data.get(i).taskEvaluationDay} " +
                            "| ${data.get(i).taskEvaluationTime} | ${data.get(i).categoryId}\n"
                )
            }
        }
    }
}
