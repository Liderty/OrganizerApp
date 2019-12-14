package com.liber.organizer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import com.liber.organizer.MainActivity
import com.liber.organizer.R
import kotlinx.android.synthetic.main.activity_add_task.*

class AddTaskActivity : AppCompatActivity() {

    lateinit var categorySpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        categorySpinner = findViewById(R.id.categorySpinner) as Spinner

        var context = this
        var db = DataBaseHandler(context)

        var categoriesList = db.readCategory()

        categorySpinner.onItemClickListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(context, "Clicked: ${position}", Toast.LENGTH_SHORT)
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }

        btnInsert.setOnClickListener {
            if (etvName.text.toString().length > 0 && etvDescription.text.toString().length > 0) {
                var task = Task(etvName.text.toString(), etvDescription.text.toString())
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
                tvResult.append("${data.get(i).taskId} | ${data.get(i).taskName} | ${data.get(i).taskDescription} | ${data.get(i).taskAvarage} | ${data.get(i).categoryId}\n")
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
