package com.liber.organizer

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DATABASE_NAME = "TaskDB"

val TASK_TABLE_NAME = "Tasks"
val COL_TASK_ID = "task_id"
val COL_TASK_NAME = "task_name"
val COL_TASK_DESCRIPTION = "task_description"
val COL_TASK_AVARAGE = "task_avarage"
val COL_TASK_ICON = "task_icon"
val COL_TASK_CREATION_DATE = "task_creation_date"
val COL_TASK_EVALUATION_DAY = "task_evaluation_day"
val COL_TASK_EVALUATION_TIME = "task_evaluation_time"
val FK_TASK_CATEGORY = "fk_category"

val GRADE_TABLE_NAME = "Grade"
val COL_GRADE_ID = "grade_id"
val COL_GRADE_GRADE = "grade_grade"
val COL_GRADE_DATE = "grade_date"
val FK_GRADE_TASK = "fk_task"

val CATEGORY_TABLE_NAME = "Categories"
val COL_CATEGORY_ID = "category_id"
val COL_CATEGORY_NAME = "category_name"
val COL_CATEGORY_ICON = "category_icon"



val QUERRY_CREATE_TABLE_CATEGORY = "CREATE TABLE ${CATEGORY_TABLE_NAME} (${COL_CATEGORY_ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${COL_CATEGORY_NAME} VARCHAR(256), ${COL_CATEGORY_ICON} INTEGER)"
val QUERRY_CREATE_TABLE_TASK = "CREATE TABLE ${TASK_TABLE_NAME} (${COL_TASK_ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${COL_TASK_NAME} VARCHAR(256), ${COL_TASK_DESCRIPTION} VARCHAR(256), ${COL_TASK_AVARAGE} INTEGER, ${COL_TASK_CREATION_DATE} INTEGER, ${COL_TASK_ICON} INTEGER, ${COL_TASK_EVALUATION_DAY} INTEGER, ${COL_TASK_EVALUATION_TIME} INTEGER, ${COL_CATEGORY_ID} INTEGER, CONSTRAINT ${FK_TASK_CATEGORY} FOREIGN KEY (${COL_CATEGORY_ID}) REFERENCES ${CATEGORY_TABLE_NAME}(${COL_CATEGORY_ID}))"
val QUERRY_CREATE_TABLE_GRADE = "CREATE TABLE ${GRADE_TABLE_NAME} (${COL_GRADE_ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${COL_GRADE_DATE} VARCHAR(256), ${COL_GRADE_GRADE} INTEGER, ${COL_TASK_ID} INTEGER, CONSTRAINT ${FK_GRADE_TASK} FOREIGN KEY (${COL_TASK_ID}) REFERENCES ${TASK_TABLE_NAME}(${COL_TASK_ID}))"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(QUERRY_CREATE_TABLE_TASK)
        db?.execSQL(QUERRY_CREATE_TABLE_GRADE)
        db?.execSQL(QUERRY_CREATE_TABLE_CATEGORY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insertTask(task: Task) {
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(COL_TASK_NAME, task.taskName)
        cv.put(COL_TASK_DESCRIPTION, task.taskDescription)
        cv.put(COL_TASK_CREATION_DATE, task.taskCreationDate)
        cv.put(COL_TASK_ICON, task.taskIcon)
        cv.put(COL_TASK_AVARAGE, task.taskAvarage)
        cv.put(COL_TASK_EVALUATION_DAY, task.taskEvaluationDay)
        cv.put(COL_TASK_EVALUATION_TIME, task.taskEvaluationTime)
        cv.put(COL_CATEGORY_ID, task.categoryId)

        var result = db.insert(TASK_TABLE_NAME, null, cv)

        if (result == -1.toLong()) {
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
        }
    }

    fun readTasks(): MutableList<Task> {
        var tasksList: MutableList<Task> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * FROM ${TASK_TABLE_NAME}"
        val selectedData = db.rawQuery(query, null)

        if (selectedData.moveToFirst()) {
            do {
                var taskItem = Task()
                taskItem.taskId = selectedData.getString(selectedData.getColumnIndex(COL_TASK_ID)).toInt()
                taskItem.taskName = selectedData.getString(selectedData.getColumnIndex(COL_TASK_NAME))
                taskItem.taskDescription = selectedData.getString(selectedData.getColumnIndex(COL_TASK_DESCRIPTION))
                taskItem.taskAvarage = selectedData.getString(selectedData.getColumnIndex(COL_TASK_AVARAGE)).toDouble()
                taskItem.taskIcon = selectedData.getString(selectedData.getColumnIndex(
                    COL_TASK_ICON)).toInt()
                taskItem.taskCreationDate = selectedData.getString(selectedData.getColumnIndex(
                    COL_TASK_CREATION_DATE)).toLong()
                taskItem.taskEvaluationDay = selectedData.getString(selectedData.getColumnIndex(
                    COL_TASK_EVALUATION_DAY)).toInt()
                taskItem.taskEvaluationTime = selectedData.getString(selectedData.getColumnIndex(
                    COL_TASK_EVALUATION_TIME)).toLong()
                taskItem.categoryId = selectedData.getString(selectedData.getColumnIndex(COL_CATEGORY_ID)).toInt()
                tasksList.add(taskItem)
            } while (selectedData.moveToNext())
        }

        selectedData.close()
        db.close()
        return tasksList
    }

    fun readTasks(categoryId: Int): MutableList<Task> {
        var tasksList: MutableList<Task> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * FROM ${TASK_TABLE_NAME} WHERE (${COL_CATEGORY_ID}==${categoryId})"
        val selectedData = db.rawQuery(query, null)

        if (selectedData.moveToFirst()) {
            do {
                var taskItem = Task()
                taskItem.taskId = selectedData.getString(selectedData.getColumnIndex(COL_TASK_ID)).toInt()
                taskItem.taskName = selectedData.getString(selectedData.getColumnIndex(COL_TASK_NAME))
                taskItem.taskDescription = selectedData.getString(selectedData.getColumnIndex(COL_TASK_DESCRIPTION))
                taskItem.taskAvarage = selectedData.getString(selectedData.getColumnIndex(COL_TASK_AVARAGE)).toDouble()
                taskItem.taskIcon = selectedData.getString(selectedData.getColumnIndex(
                    COL_TASK_ICON)).toInt()
                taskItem.taskCreationDate = selectedData.getString(selectedData.getColumnIndex(
                    COL_TASK_CREATION_DATE)).toLong()
                taskItem.taskEvaluationDay = selectedData.getString(selectedData.getColumnIndex(
                    COL_TASK_EVALUATION_DAY)).toInt()
                taskItem.taskEvaluationTime = selectedData.getString(selectedData.getColumnIndex(
                    COL_TASK_EVALUATION_TIME)).toLong()
                taskItem.categoryId = selectedData.getString(selectedData.getColumnIndex(COL_CATEGORY_ID)).toInt()
                tasksList.add(taskItem)
            } while (selectedData.moveToNext())
        }

        selectedData.close()
        db.close()
        return tasksList
    }

    fun updateTaskAvarage(taskId: Int, newTaskAvarage: Double) {
        val db = this.writableDatabase
        val query = "SELECT * FROM ${TASK_TABLE_NAME} WHERE (${COL_TASK_ID}==${taskId})"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var cv = ContentValues()
                cv.put(COL_TASK_AVARAGE, newTaskAvarage)

                db.update(
                    TASK_TABLE_NAME, cv, "${COL_TASK_ID}=? AND ${COL_TASK_NAME}=?", arrayOf(
                        result.getString(
                            result.getColumnIndex(
                                COL_TASK_ID
                            )
                        ), result.getString(
                            result.getColumnIndex(
                                COL_TASK_NAME
                            )
                        )
                    )
                )
            } while (result.moveToNext())
        }
        result.close()
        db.close()
    }

//
//    fun getNames(): MutableList<String> {
//        var list: MutableList<String> = ArrayList()
//
//        val db = this.readableDatabase
//        val query = "SELECT * FROM ${TABLE_NAME}"
//        val result = db.rawQuery(query, null)
//
//        if (result.moveToFirst()) {
//            do {
//                list.add(result.getString(result.getColumnIndex(COL_NAME)))
//            } while (result.moveToNext())
//        }
//
//        result.close()
//        db.close()
//        return list
//    }
//    fun deleteData() {
//        var db = this.writableDatabase
//
//        db.delete(TABLE_NAME, COL_ID + "=?", arrayOf(1.toString()))
//
//        db.close()
//    }

    fun insertGrade(grade: Grade) {
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(COL_GRADE_GRADE, grade.gradeGrade)
        cv.put(COL_GRADE_DATE, grade.gradeDate)
        cv.put(COL_TASK_ID, grade.taskId)

        var result = db.insert(GRADE_TABLE_NAME, null, cv)

        if (result == -1.toLong()) {
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
        }
    }

    fun readGrades(): MutableList<Grade> {
        var gradeList: MutableList<Grade> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * FROM ${GRADE_TABLE_NAME}"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var gradeItem = Grade()
                gradeItem.gradeId = result.getString(result.getColumnIndex(COL_GRADE_ID)).toInt()
                gradeItem.gradeDate = result.getString(result.getColumnIndex(COL_GRADE_DATE)).toLong()
                gradeItem.gradeGrade = result.getString(result.getColumnIndex(COL_GRADE_GRADE)).toInt()
                gradeItem.taskId = result.getString(result.getColumnIndex(COL_TASK_ID)).toInt()
                gradeList.add(gradeItem)
            } while (result.moveToNext())
        }

        result.close()
        db.close()
        return gradeList
    }

    fun insertCategory(category: Category) {
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(COL_CATEGORY_NAME, category.categoryName)
        cv.put(COL_CATEGORY_ICON, category.categoryIcon)

        var result = db.insert(CATEGORY_TABLE_NAME, null, cv)

        if (result == -1.toLong()) {
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
        }
    }

    fun readCategory(): MutableList<Category> {
        var categoryList: MutableList<Category> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * FROM ${CATEGORY_TABLE_NAME}"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var categoryItem = Category()
                categoryItem.categoryId = result.getString(result.getColumnIndex(COL_CATEGORY_ID)).toInt()
                categoryItem.categoryName = result.getString(result.getColumnIndex(COL_CATEGORY_NAME))
                categoryItem.categoryIcon = result.getString(result.getColumnIndex(COL_CATEGORY_ICON)).toInt()

                categoryList.add(categoryItem)
            } while (result.moveToNext())
        }

        result.close()
        db.close()

        return categoryList
    }
}