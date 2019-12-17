package com.liber.organizer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.liber.organizer.MainActivity
import com.liber.organizer.R
import kotlinx.android.synthetic.main.activity_add_task.*

class AddTaskActivity : AppCompatActivity() {
    var context = this
    var db = DataBaseHandler(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val categoryItem = intent.getSerializableExtra("category") as Category

        evaluation_interval_radio.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                Toast.makeText(applicationContext," On checked change :"+
                        " ${radio.text}",
                    Toast.LENGTH_SHORT).show()
            })


        btnInsert.setOnClickListener {
            if (etvName.text.toString().length > 0 && etvDescription.text.toString().length > 0) {
                var task = Task(etvName.text.toString(), etvDescription.text.toString(), categoryItem.categoryId)
                db.insertTask(task)
            } else {
                Toast.makeText(context, "Please fill data!", Toast.LENGTH_SHORT)
            }
        }

        btnGoBack.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnRead.setOnClickListener {
            var data = db.readTasks()
            tvResult.text = ""

            for (i in 0..(data.size - 1)) {
                tvResult.append(
                    "${data.get(i).taskId} | ${data.get(i).taskName} | ${data.get(i).taskDescription} | ${data.get(
                        i
                    ).taskAvarage} | ${data.get(i).categoryId}\n"
                )
            }
        }
    }
}
