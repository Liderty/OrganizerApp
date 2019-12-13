package com.liber.organizer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.liber.organizer.MainActivity
import com.liber.organizer.R
import kotlinx.android.synthetic.main.activity_add_task.*

class AddTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        var context = this
        var db = DataBaseHandler(context)


        btnGoBack.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnInsert.setOnClickListener {
            if (etvName.text.toString().length > 0 && etvDescription.text.toString().length > 0) {
                var task = Task(etvName.text.toString(), etvDescription.text.toString())
                db.insertTask(task)
            } else {
                Toast.makeText(context, "Please fill data!", Toast.LENGTH_SHORT)
            }
        }

        btnRead.setOnClickListener {
            var data = db.readTasks()
            tvResult.text = ""

            for (i in 0..(data.size - 1)) {
                tvResult.append("${data.get(i).taskId} | ${data.get(i).taskName} | ${data.get(i).taskDescription} | ${data.get(i).taskAvarage}\n")
            }
        }

//        btnUpdate.setOnClickListener {
//            db.updateData()
//            btnRead.performClick()
//        }
//
//        btnDelete.setOnClickListener {
//            db.deleteData()
//            btnRead.performClick()
//        }
    }
}
