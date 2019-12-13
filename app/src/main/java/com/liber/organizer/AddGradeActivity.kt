package com.liber.organizer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.liber.organizer.MainActivity
import com.liber.organizer.R
import kotlinx.android.synthetic.main.activity_add_grade.*

class AddGradeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_grade)

        var context = this
        var db = DataBaseHandler(context)


        btnGoBack.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnInsert.setOnClickListener {
            if (etvGrade.text.toString().length > 0 && etvTaskId.text.toString().length > 0) {
                var grade =
                    Grade(etvGrade.text.toString().toInt(), etvTaskId.text.toString().toInt())
                db.insertGrade(grade)
            } else {
                Toast.makeText(context, "Please fill data!", Toast.LENGTH_SHORT).show()
            }
        }

        btnRead.setOnClickListener {
            var data = db.readGrades()
            tvResult.text = ""

            for (i in 0..(data.size - 1)) {
                tvResult.append(
                    "${data.get(i).gradeId} | ${data.get(i).gradeGrade} | ${data.get(i).gradeDate} | ${data.get(i).taskId}\n"
                )
            }
        }
    }
}
