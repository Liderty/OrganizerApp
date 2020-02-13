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
val COL_TASK_UPDATE_DATE = "task_update_date"
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

val GOAL_TABLE_NAME = "Goal"
val COL_GOAL_ID = "goal_id"
val COL_GOAL_CONTENT = "goal_content"
val COL_GOAL_STATUS = "goal_status"

val EMPTY_GRADE = 0
val GOAL_DONE_VALUE = 1
val GOAL_PROGRESS_VALUE = 0

val QUERY_CREATE_TABLE_CATEGORY =
    "CREATE TABLE ${CATEGORY_TABLE_NAME} (${COL_CATEGORY_ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${COL_CATEGORY_NAME} VARCHAR(256), ${COL_CATEGORY_ICON} INTEGER)"

val QUERY_CREATE_TABLE_TASK =
    "CREATE TABLE ${TASK_TABLE_NAME} (${COL_TASK_ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${COL_TASK_NAME} VARCHAR(256), ${COL_TASK_DESCRIPTION} VARCHAR(256), ${COL_TASK_AVARAGE} INTEGER, ${COL_TASK_UPDATE_DATE} INTEGER, ${COL_TASK_ICON} INTEGER, ${COL_TASK_EVALUATION_DAY} INTEGER, ${COL_TASK_EVALUATION_TIME} INTEGER, ${COL_CATEGORY_ID} INTEGER, CONSTRAINT ${FK_TASK_CATEGORY} FOREIGN KEY (${COL_CATEGORY_ID}) REFERENCES ${CATEGORY_TABLE_NAME}(${COL_CATEGORY_ID}))"

val QUERY_CREATE_TABLE_GRADE =
    "CREATE TABLE ${GRADE_TABLE_NAME} (${COL_GRADE_ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${COL_GRADE_DATE} VARCHAR(256), ${COL_GRADE_GRADE} INTEGER, ${COL_TASK_ID} INTEGER, CONSTRAINT ${FK_GRADE_TASK} FOREIGN KEY (${COL_TASK_ID}) REFERENCES ${TASK_TABLE_NAME}(${COL_TASK_ID}))"

val QUERY_CREATE_TABLE_GOAL = "CREATE TABLE ${GOAL_TABLE_NAME} (${COL_GOAL_ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${COL_GOAL_STATUS} INTEGER(4), ${COL_GOAL_CONTENT} VARCHAR(256), ${COL_TASK_ID} INTEGER)"


class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(QUERY_CREATE_TABLE_TASK)
        db?.execSQL(QUERY_CREATE_TABLE_GRADE)
        db?.execSQL(QUERY_CREATE_TABLE_CATEGORY)
        db?.execSQL(QUERY_CREATE_TABLE_GOAL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insertTask(task: Task) {
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(COL_TASK_NAME, task.taskName)
        cv.put(COL_TASK_DESCRIPTION, task.taskDescription)
        cv.put(COL_TASK_UPDATE_DATE, task.taskUpdateDate)
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

        while (selectedData.moveToNext()) {
                var taskItem = Task()
                taskItem.taskId =
                    selectedData.getString(selectedData.getColumnIndex(COL_TASK_ID)).toInt()
                taskItem.taskName =
                    selectedData.getString(selectedData.getColumnIndex(COL_TASK_NAME))
                taskItem.taskDescription =
                    selectedData.getString(selectedData.getColumnIndex(COL_TASK_DESCRIPTION))
                taskItem.taskAvarage =
                    selectedData.getString(selectedData.getColumnIndex(COL_TASK_AVARAGE)).toDouble()

                taskItem.taskIcon = selectedData.getString(selectedData.getColumnIndex(COL_TASK_ICON)).toInt()
                taskItem.taskUpdateDate = selectedData.getString(selectedData.getColumnIndex(
                    COL_TASK_UPDATE_DATE)).toLong()

                taskItem.taskEvaluationDay = selectedData.getString(
                    selectedData.getColumnIndex(
                        COL_TASK_EVALUATION_DAY
                    )
                ).toInt()
                taskItem.taskEvaluationTime = selectedData.getString(
                    selectedData.getColumnIndex(
                        COL_TASK_EVALUATION_TIME
                    )
                ).toLong()
                taskItem.categoryId =
                    selectedData.getString(selectedData.getColumnIndex(COL_CATEGORY_ID)).toInt()
                tasksList.add(taskItem)
            }

        selectedData.close()
        db.close()
        return tasksList
    }

    fun readTask(taskId: Int): Task {
        val db = this.readableDatabase
        val query = "SELECT * FROM ${TASK_TABLE_NAME} WHERE (${COL_TASK_ID}==${taskId})"
        val selectedData = db.rawQuery(query, null)

        var taskItem = Task()

        if (selectedData.moveToFirst()) {
            do {
                taskItem.taskId =
                    selectedData.getString(selectedData.getColumnIndex(COL_TASK_ID)).toInt()
                taskItem.taskName =
                    selectedData.getString(selectedData.getColumnIndex(COL_TASK_NAME))
                taskItem.taskDescription =
                    selectedData.getString(selectedData.getColumnIndex(COL_TASK_DESCRIPTION))
                taskItem.taskAvarage =
                    selectedData.getString(selectedData.getColumnIndex(COL_TASK_AVARAGE)).toDouble()
                taskItem.taskIcon = selectedData.getString(
                    selectedData.getColumnIndex(
                        COL_TASK_ICON
                    )
                ).toInt()
                taskItem.taskUpdateDate = selectedData.getString(
                    selectedData.getColumnIndex(
                        COL_TASK_UPDATE_DATE
                    )
                ).toLong()
                taskItem.taskEvaluationDay = selectedData.getString(
                    selectedData.getColumnIndex(
                        COL_TASK_EVALUATION_DAY
                    )
                ).toInt()
                taskItem.taskEvaluationTime = selectedData.getString(
                    selectedData.getColumnIndex(
                        COL_TASK_EVALUATION_TIME
                    )
                ).toLong()
                taskItem.categoryId =
                    selectedData.getString(selectedData.getColumnIndex(COL_CATEGORY_ID)).toInt()
            } while (selectedData.moveToNext())
        }

        selectedData.close()
        db.close()
        return taskItem
    }


    fun readTasks(categoryId: Int): MutableList<Task> {
        var tasksList: MutableList<Task> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * FROM ${TASK_TABLE_NAME} WHERE (${COL_CATEGORY_ID}==${categoryId})"
        val selectedData = db.rawQuery(query, null)

        if (selectedData.moveToFirst()) {
            do {
                var taskItem = Task()
                taskItem.taskId =
                    selectedData.getString(selectedData.getColumnIndex(COL_TASK_ID)).toInt()
                taskItem.taskName =
                    selectedData.getString(selectedData.getColumnIndex(COL_TASK_NAME))
                taskItem.taskDescription =
                    selectedData.getString(selectedData.getColumnIndex(COL_TASK_DESCRIPTION))
                taskItem.taskAvarage =
                    selectedData.getString(selectedData.getColumnIndex(COL_TASK_AVARAGE)).toDouble()
                taskItem.taskIcon = selectedData.getString(
                    selectedData.getColumnIndex(
                        COL_TASK_ICON
                    )
                ).toInt()
                taskItem.taskUpdateDate = selectedData.getString(
                    selectedData.getColumnIndex(
                        COL_TASK_UPDATE_DATE
                    )
                ).toLong()
                taskItem.taskEvaluationDay = selectedData.getString(
                    selectedData.getColumnIndex(
                        COL_TASK_EVALUATION_DAY
                    )
                ).toInt()
                taskItem.taskEvaluationTime = selectedData.getString(
                    selectedData.getColumnIndex(
                        COL_TASK_EVALUATION_TIME
                    )
                ).toLong()
                taskItem.categoryId =
                    selectedData.getString(selectedData.getColumnIndex(COL_CATEGORY_ID)).toInt()
                tasksList.add(taskItem)
            } while (selectedData.moveToNext())
        }

        selectedData.close()
        db.close()
        return tasksList
    }

    fun updateTask(taskId: Int, editedTask: Task) {

        val db = this.writableDatabase
        val query = "SELECT * FROM ${TASK_TABLE_NAME} WHERE (${COL_TASK_ID}==${taskId})"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val cv = ContentValues()

                val emptyTask = Task()

                if (emptyTask.taskName != editedTask.taskName) {
                    cv.put(COL_TASK_NAME, editedTask.taskName)
                }

                if (emptyTask.taskDescription != editedTask.taskDescription) {
                    cv.put(COL_TASK_DESCRIPTION, editedTask.taskDescription)
                }

                if (emptyTask.taskIcon != editedTask.taskIcon) {
                    cv.put(COL_TASK_ICON, editedTask.taskIcon)
                }

                if (emptyTask.taskEvaluationDay != editedTask.taskEvaluationDay) {
                    cv.put(COL_TASK_EVALUATION_DAY, editedTask.taskEvaluationDay)
                }

                if (emptyTask.taskEvaluationTime != editedTask.taskEvaluationTime) {
                    cv.put(COL_TASK_EVALUATION_TIME, editedTask.taskEvaluationTime)
                }

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

    fun updateTaskUpdateDate(taskId: Int, newDate: Long) {
        val db = this.writableDatabase
        val query = "SELECT * FROM ${TASK_TABLE_NAME} WHERE (${COL_TASK_ID}==${taskId})"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var cv = ContentValues()
                cv.put(COL_TASK_UPDATE_DATE, newDate)

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

    fun deleteTask(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success =
            db.delete(TASK_TABLE_NAME, COL_TASK_ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

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
                gradeItem.gradeDate =
                    result.getString(result.getColumnIndex(COL_GRADE_DATE)).toLong()
                gradeItem.gradeGrade =
                    result.getString(result.getColumnIndex(COL_GRADE_GRADE)).toInt()
                gradeItem.taskId = result.getString(result.getColumnIndex(COL_TASK_ID)).toInt()
                gradeList.add(gradeItem)
            } while (result.moveToNext())
        }

        result.close()
        db.close()
        return gradeList
    }

    fun readGrades(taskId: Int): MutableList<Grade> {
        var gradeList: MutableList<Grade> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * FROM ${GRADE_TABLE_NAME} WHERE (${COL_TASK_ID}==${taskId})"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var gradeItem = Grade()
                gradeItem.gradeId = result.getString(result.getColumnIndex(COL_GRADE_ID)).toInt()
                gradeItem.gradeDate =
                    result.getString(result.getColumnIndex(COL_GRADE_DATE)).toLong()
                gradeItem.gradeGrade =
                    result.getString(result.getColumnIndex(COL_GRADE_GRADE)).toInt()
                gradeItem.taskId = result.getString(result.getColumnIndex(COL_TASK_ID)).toInt()
                gradeList.add(gradeItem)
            } while (result.moveToNext())
        }

        result.close()
        db.close()
        return gradeList
    }

    fun readEmptyGrades(): MutableList<Grade> {
        var gradeList: MutableList<Grade> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * FROM ${GRADE_TABLE_NAME} WHERE (${COL_GRADE_GRADE}==${EMPTY_GRADE})"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var gradeItem = Grade()
                gradeItem.gradeId = result.getString(result.getColumnIndex(COL_GRADE_ID)).toInt()
                gradeItem.gradeDate =
                    result.getString(result.getColumnIndex(COL_GRADE_DATE)).toLong()
                gradeItem.gradeGrade =
                    result.getString(result.getColumnIndex(COL_GRADE_GRADE)).toInt()
                gradeItem.taskId = result.getString(result.getColumnIndex(COL_TASK_ID)).toInt()
                gradeList.add(gradeItem)
            } while (result.moveToNext())
        }

        result.close()
        db.close()
        return gradeList
    }

    fun updateGradeGrade(gradeId: Int, newGrade: Int) {
        val db = this.writableDatabase
        val query = "SELECT * FROM ${GRADE_TABLE_NAME} WHERE (${COL_GRADE_ID}==${gradeId})"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var cv = ContentValues()
                cv.put(COL_GRADE_GRADE, newGrade)

                db.update(
                    GRADE_TABLE_NAME, cv, "${COL_GRADE_ID}=? AND ${COL_GRADE_DATE}=?", arrayOf(
                        result.getString(
                            result.getColumnIndex(
                                COL_GRADE_ID
                            )
                        ), result.getString(
                            result.getColumnIndex(
                                COL_GRADE_DATE
                            )
                        )
                    )
                )
            } while (result.moveToNext())
        }
        result.close()
        db.close()
    }

    fun deleteAllTaskGrades(_id: Int) {
        val db = this.writableDatabase
        db.delete(GRADE_TABLE_NAME, COL_TASK_ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
    }

    fun deleteAllTaskGoals(_id: Int) {
        val db = this.writableDatabase
        db.delete(GOAL_TABLE_NAME, COL_TASK_ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
    }

    fun insertCategory(category: Category) {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COL_CATEGORY_NAME, category.categoryName)
        cv.put(COL_CATEGORY_ICON, category.categoryIcon)

        val result = db.insert(CATEGORY_TABLE_NAME, null, cv)

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
                categoryItem.categoryId =
                    result.getString(result.getColumnIndex(COL_CATEGORY_ID)).toInt()
                categoryItem.categoryName =
                    result.getString(result.getColumnIndex(COL_CATEGORY_NAME))
                categoryItem.categoryIcon =
                    result.getString(result.getColumnIndex(COL_CATEGORY_ICON)).toInt()

                categoryList.add(categoryItem)
            } while (result.moveToNext())
        }

        result.close()
        db.close()

        return categoryList
    }

    fun readCategory(categoryId: Int): Category {
        val db = this.readableDatabase
        val query = "SELECT * FROM ${CATEGORY_TABLE_NAME} WHERE (${COL_CATEGORY_ID}==${categoryId})"
        val result = db.rawQuery(query, null)
        val categoryItem = Category()

        if (result.moveToFirst()) {
            do {
                categoryItem.categoryId =
                    result.getString(result.getColumnIndex(COL_CATEGORY_ID)).toInt()
                categoryItem.categoryName =
                    result.getString(result.getColumnIndex(COL_CATEGORY_NAME))
                categoryItem.categoryIcon =
                    result.getString(result.getColumnIndex(COL_CATEGORY_ICON)).toInt()
            } while (result.moveToNext())
        }

        result.close()
        db.close()

        return categoryItem
    }

    fun updateCategory(categoryId: Int, category: Category) {
        val db = this.writableDatabase
        val query = "SELECT * FROM ${CATEGORY_TABLE_NAME} WHERE (${COL_CATEGORY_ID}==${categoryId})"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var cv = ContentValues()
                val emptyCategory = Category()

                if (emptyCategory.categoryName != category.categoryName) {
                    cv.put(COL_CATEGORY_NAME, category.categoryName)
                }

                if (emptyCategory.categoryIcon != category.categoryIcon) {
                    cv.put(COL_CATEGORY_ICON, category.categoryIcon)
                }

                db.update(
                    CATEGORY_TABLE_NAME,
                    cv,
                    "${COL_CATEGORY_ID}=? AND ${COL_CATEGORY_NAME}=?",
                    arrayOf(
                        result.getString(
                            result.getColumnIndex(
                                COL_CATEGORY_ID
                            )
                        ), result.getString(
                            result.getColumnIndex(
                                COL_CATEGORY_NAME
                            )
                        )
                    )
                )
            } while (result.moveToNext())
        }
        result.close()
        db.close()
    }

    fun deleteCategory(_id: Int) {
        val db = this.writableDatabase
        db.delete(CATEGORY_TABLE_NAME, COL_CATEGORY_ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
    }

    fun insertGoal(goal: Goal) {
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(COL_GOAL_CONTENT, goal.goalContent)
        cv.put(COL_TASK_ID, goal.goalTaskId)
        cv.put(COL_GOAL_STATUS, goal.goalStatus)

        var result = db.insert(GOAL_TABLE_NAME, null, cv)

        if (result == -1.toLong()) {
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
        }
    }

    fun readGoals(taskId: Int): MutableList<Goal> {
        var goalsList: MutableList<Goal> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * FROM ${GOAL_TABLE_NAME} WHERE (${COL_TASK_ID}==${taskId})"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var goalItem = Goal()
                goalItem.goalId =
                    result.getString(result.getColumnIndex(COL_GOAL_ID)).toInt()

                goalItem.goalContent =
                    result.getString(result.getColumnIndex(COL_GOAL_CONTENT))

                goalItem.goalStatus =
                    result.getString(result.getColumnIndex(COL_GOAL_STATUS)).toInt()

                goalItem.goalTaskId =
                    result.getString(result.getColumnIndex(COL_TASK_ID)).toInt()

                goalsList.add(goalItem)
            } while (result.moveToNext())
        }

        result.close()
        db.close()

        return goalsList
    }

    fun readGoalsInProgess(taskId: Int): MutableList<Goal> {
        var goalsList: MutableList<Goal> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * FROM ${GOAL_TABLE_NAME} WHERE (${COL_TASK_ID}==${taskId} AND ${COL_GOAL_STATUS}==${GOAL_PROGRESS_VALUE})"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var goalItem = Goal()
                goalItem.goalId =
                    result.getString(result.getColumnIndex(COL_GOAL_ID)).toInt()

                goalItem.goalContent =
                    result.getString(result.getColumnIndex(COL_GOAL_CONTENT))

                goalItem.goalStatus =
                    result.getString(result.getColumnIndex(COL_GOAL_STATUS)).toInt()

                goalItem.goalTaskId =
                    result.getString(result.getColumnIndex(COL_TASK_ID)).toInt()

                goalsList.add(goalItem)
            } while (result.moveToNext())
        }

        result.close()
        db.close()

        return goalsList
    }

    fun updateGoalStatus(goalId: Int) {
        val db = this.writableDatabase
        val query = "SELECT * FROM ${GOAL_TABLE_NAME} WHERE (${COL_GOAL_ID}==${goalId})"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var cv = ContentValues()

                cv.put(COL_GOAL_STATUS, GOAL_DONE_VALUE)

                db.update(
                    GOAL_TABLE_NAME,
                    cv,
                    "${COL_GOAL_ID}=? AND ${COL_GOAL_CONTENT}=?",
                    arrayOf(
                        result.getString(
                            result.getColumnIndex(
                                COL_GOAL_ID
                            )
                        ), result.getString(
                            result.getColumnIndex(
                                COL_GOAL_CONTENT
                            )
                        )
                    )
                )
            } while (result.moveToNext())
        }
        result.close()
        db.close()
    }


}