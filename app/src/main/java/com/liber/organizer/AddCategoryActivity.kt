package com.liber.organizer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_category.*

class AddCategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        var context = this
        var db = DataBaseHandler(context)

        btnInsert.setOnClickListener {
            if (etvName.text.toString().length > 0) {
                var category = Category(etvName.text.toString())
                db.insertCategory(category)
            } else {
                Toast.makeText(context, "Please fill data!", Toast.LENGTH_SHORT)
            }
        }

        btnGoBack.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnRead.setOnClickListener {
            var data = db.readCategory()
            tvResult.text = ""

            for (i in 0..(data.size - 1)) {
                tvResult.append("${data.get(i).categoryId} | ${data.get(i).categoryName} | ${data.get(i).categoryIcon}\n")
            }
        }
    }
}
