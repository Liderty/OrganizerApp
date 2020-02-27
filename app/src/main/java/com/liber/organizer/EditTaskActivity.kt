package com.liber.organizer

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.activity_edit_task.*
import kotlinx.android.synthetic.main.activity_edit_task.btnGoBack
import kotlinx.android.synthetic.main.activity_edit_task.daySpinnerElement
import kotlinx.android.synthetic.main.activity_edit_task.daysSpinner
import kotlinx.android.synthetic.main.activity_edit_task.etvDescription
import kotlinx.android.synthetic.main.activity_edit_task.etvName
import kotlinx.android.synthetic.main.activity_edit_task.evaluation_interval_radio_group
import kotlinx.android.synthetic.main.activity_edit_task.radio_everyday
import kotlinx.android.synthetic.main.activity_edit_task.radio_weekly
import kotlinx.android.synthetic.main.activity_edit_task.taskImage
import kotlinx.android.synthetic.main.activity_edit_task.timePicker

class EditTaskActivity : AppCompatActivity() {

    val EVALUATE_EVRERYDAY_VALUE = 7
    val REQUEST_ICON_CODE = 1

    var context = this
    var db = DataBaseHandler(context)
    var evaluation_flag = false
    var time_changed_flag = false
    var selectedDay = 0
    var selectedTime = 0L
    var taskIcon = R.drawable.star


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        val taskItem = intent.getSerializableExtra("task") as Task

        val daysSpinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.daysOfWeek)
        )

        taskImage.setOnClickListener {
            var intent = Intent(this, IconPickerActivity::class.java)
            startActivityForResult(intent, REQUEST_ICON_CODE)
        }

        daysSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        daysSpinner.adapter = daysSpinnerAdapter
        setTaskValues(taskItem)

        radio_everyday.setOnClickListener {
            this.evaluation_flag = false
            daySpinnerElement.visibility = View.INVISIBLE
        }

        radio_weekly.setOnClickListener {
            this.evaluation_flag = true
            daySpinnerElement.visibility = View.VISIBLE
        }

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
                    "Spinner selected : ${parent?.getItemAtPosition(position).toString()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        timePicker.setOnTimeChangedListener { _, hour, minute ->
            var hoursInMillis = hour * 60L * 60 * 1000
            var minutesInMillis = minute * 60L * 1000
            selectedTime = hoursInMillis + minutesInMillis
            time_changed_flag = true
        }

        btnSaveChanges.setOnClickListener {
            if (etvName.text.toString().length > 0 && etvDescription.text.toString().length > 0) {
                var isEditedFlag = false
                val editedTask = Task()

                if(etvName.text.toString() != taskItem.taskName) {
                    isEditedFlag = true
                    editedTask.taskName = etvName.text.toString()
                }

                if(etvDescription.text.toString() != taskItem.taskDescription) {
                    isEditedFlag = true
                    editedTask.taskDescription = etvDescription.text.toString()
                }

                if(taskIcon != taskItem.taskIcon) {
                    isEditedFlag = true
                    editedTask.taskIcon = taskIcon
                }

                if(evaluation_flag) {
                    if (selectedDay != taskItem.taskEvaluationDay) {
                        isEditedFlag = true
                        editedTask.taskEvaluationDay = selectedDay
                    }
                } else {
                    if (EVALUATE_EVRERYDAY_VALUE > taskItem.taskEvaluationDay) {
                        isEditedFlag = true
                        editedTask.taskEvaluationDay = EVALUATE_EVRERYDAY_VALUE
                    }
                }

                if((time_changed_flag) && (selectedTime != taskItem.taskEvaluationTime)) {
                    isEditedFlag = true
                    editedTask.taskEvaluationTime = selectedTime
                }

                if(isEditedFlag) {
                    db.updateTask(taskItem.taskId, editedTask)
                    Toast.makeText(context, "Task updated!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(context, "Nothing changed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please fill data!", Toast.LENGTH_SHORT).show()
            }
        }

        btnGoBack.setOnClickListener {
            finish()
        }
    }

    fun setTaskValues(taskItem: Task) {
        etvName.setText(taskItem.taskName)
        taskImage.setImageDrawable(this.resources.getDrawable(taskItem.taskIcon))
        etvDescription.setText(taskItem.taskDescription)

        if (taskItem.taskEvaluationDay <= 6) {
            evaluation_flag = true
            evaluation_interval_radio_group.check(radio_weekly.id)
            daySpinnerElement.visibility = View.VISIBLE
            Toast.makeText(context, "${taskItem.taskEvaluationDay}", Toast.LENGTH_LONG).show()
            daysSpinner.setSelection(taskItem.taskEvaluationDay)
        }

        val taskTime = TaskDate(taskItem.taskEvaluationTime)
        timePicker.hour = taskTime.getHour()
        timePicker.minute = taskTime.getMinute()
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
