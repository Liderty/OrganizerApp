package com.liber.organizer

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_category.*
import kotlinx.android.synthetic.main.activity_add_category.btnGoBack
import kotlinx.android.synthetic.main.activity_add_category.btnInsert
import kotlinx.android.synthetic.main.activity_add_category.btnRead
import kotlinx.android.synthetic.main.activity_add_category.etvName
import kotlinx.android.synthetic.main.activity_add_category.tvResult

class AddCategoryActivity : AppCompatActivity() {

    val REQUEST_ICON_CODE = 1
    val context = this

    var categoryIcon = R.drawable.settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        var db = DataBaseHandler(context)

        categoryImage.setOnClickListener {
            var intent = Intent(this, IconPickerActivity::class.java)
            startActivityForResult(intent, REQUEST_ICON_CODE)
        }

        btnInsert.setOnClickListener {
            if (etvName.text.toString().length > 0) {
                var category = Category(etvName.text.toString(), categoryIcon)
                db.insertCategory(category)

                finish()
            } else {
                Toast.makeText(context, "Please fill data!", Toast.LENGTH_SHORT)
            }
        }

        btnGoBack.setOnClickListener {
            finish()
        }

        btnRead.setOnClickListener {
            var data = db.readCategory()
            tvResult.text = ""

            for (i in 0..(data.size - 1)) {
                tvResult.append("${data.get(i).categoryId} | ${data.get(i).categoryName} | ${data.get(i).categoryIcon}\n")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ICON_CODE && resultCode == Activity.RESULT_OK) {
            val test = data?.getSerializableExtra("klucz") as Int
            categoryIcon = test
            categoryImage.setImageDrawable(context.getDrawable(test))
        }
    }
}
