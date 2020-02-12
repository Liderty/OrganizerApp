package com.liber.organizer

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_edit_category.*


class EditCategoryActivity : AppCompatActivity() {

    val REQUEST_ICON_CODE = 1
    val context = this

    var categoryIcon = R.drawable.settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)

        var db = DataBaseHandler(context)
        val categoryItem = intent.getSerializableExtra("category") as Category

        setUpCategoryData(categoryItem)

        categoryImage.setOnClickListener {
            var intent = Intent(this, IconPickerActivity::class.java)
            startActivityForResult(intent, REQUEST_ICON_CODE)
        }

        buttonSaveChanges.setOnClickListener {
            if (etvName.text.toString().length > 0) {
                if(categoryItem.categoryName != etvName.text.toString() || categoryItem.categoryIcon != categoryIcon) {
                    val emptyCategory = Category()

                    if(categoryItem.categoryName != etvName.text.toString()) {
                        emptyCategory.categoryName = etvName.text.toString()
                    }

                    if(categoryItem.categoryIcon != categoryIcon) {
                        emptyCategory.categoryIcon = categoryIcon
                    }

                    db.updateCategory(categoryItem.categoryId, emptyCategory)
                }

                finish()

            } else {
                Toast.makeText(context, "Please fill data!", Toast.LENGTH_SHORT).show()
            }
        }

        buttonGoBack.setOnClickListener {
            finish()
        }

        buttonDeleteCategory.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Deleting Category")
            builder.setMessage("WARNING: Are you sure you want to delete this CATEGORY? All of its data like tasks, grades, goals and statistics will be also deleted!")

            builder.setPositiveButton("Confirm Delete") { dialog, which ->
                val taskList = db.readTasks(categoryItem.categoryId)

                for(taskItem in taskList) {
                    if (db.deleteTask(taskItem.taskId)) {
                        db.deleteAllTaskGrades(taskItem.taskId)
                        db.deleteAllTaskGoals(taskItem.taskId)
                    }
                }

                db.deleteCategory(categoryItem.categoryId)

                Toast.makeText(
                    applicationContext,
                    "Successfully deleted.",
                    Toast.LENGTH_SHORT
                ).show()

                var intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                Toast.makeText(applicationContext, "Nothing deleted", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
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

    fun setUpCategoryData(category: Category) {
        etvName.setText(category.categoryName)
        categoryImage.setImageResource(category.categoryIcon)
    }
}
