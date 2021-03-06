package com.liber.organizer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_add_task.*

class AddTaskActivity : AppCompatActivity() {

    val EVALUATE_EVRERYDAY_VALUE = 7
    val REQUEST_ICON_CODE = 1

    var context = this
    var db = DataBaseHandler(context)
    var evaluation_flag = false
    var selectedDay = 0
    var selectedTime = 0L
    var taskIcon = R.drawable.star

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val categoryItem = intent.getSerializableExtra("category") as Category
        val daySpinner = findViewById(R.id.daySpinnerElement) as LinearLayout

        selectedTime = TaskDate().getTimeWithoutDate()

        taskImage.setOnClickListener {
            var intent = Intent(this, IconPickerActivity::class.java)
            startActivityForResult(intent, REQUEST_ICON_CODE)
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
            val hoursInMillis = hour * 60L * 60 * 1000
            val minutesInMillis = minute * 60L * 1000
            selectedTime = hoursInMillis + minutesInMillis
        }


        btnInsert.setOnClickListener {
            if (etvName.text.toString().length > 0 && etvDescription.text.toString().length > 0) {

                if(evaluation_flag) {
                    val task = Task(
                        etvName.text.toString(),
                        taskIcon,
                        etvDescription.text.toString(),
                        selectedDay,
                        selectedTime,
                        categoryItem.categoryId
                    )
                    db.insertTask(task)

                } else {
                    val task = Task(
                        etvName.text.toString(),
                        taskIcon,
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
            finish()
        }

        fun devDoneAdd (numberOfDays: Long){
            if (etvName.text.toString().length > 0 && etvDescription.text.toString().length > 0) {
                if (evaluation_flag) {
                    val task = Task(
                        etvName.text.toString(),
                        taskIcon,
                        etvDescription.text.toString(),
                        selectedDay,
                        selectedTime,
                        categoryItem.categoryId
                    )

                    task.taskUpdateDate = numberOfDays

                    db.insertTask(task)

                } else {

                    val task = Task(
                        etvName.text.toString(),
                        taskIcon,
                        etvDescription.text.toString(),
                        EVALUATE_EVRERYDAY_VALUE,
                        selectedTime,
                        categoryItem.categoryId
                    )

                    task.taskUpdateDate = numberOfDays

                    db.insertTask(task)

                }

                finish()
            }
        }

        btnInsert5.setOnClickListener {
            var newDate = TaskDate()
            newDate.add(-(newDate.DAY_IN_MILLIS * 1))
            devDoneAdd(newDate.milliseconds)
        }

        btnInsert15.setOnClickListener {
            var newDate = TaskDate()
            newDate.add(-(newDate.DAY_IN_MILLIS * 5))
            devDoneAdd(newDate.milliseconds)
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ICON_CODE && resultCode == Activity.RESULT_OK) {
            val test = data?.getSerializableExtra("klucz") as Int
            taskIcon = test
            taskImage.setImageDrawable(context.getDrawable(test))
        }
    }
}
