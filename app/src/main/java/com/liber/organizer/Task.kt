package com.liber.organizer

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable

class Task : Serializable {

    var taskId: Int = 0
    var taskName: String = ""
    var taskDescription: String = ""
    var taskAvarage: Double = 0.0
    var taskIcon: Int = R.drawable.settings
    var taskUpdateDate: Long = 0
    var taskEvaluationDay: Int = 0
    var taskEvaluationTime: Long = 0
    var categoryId: Int = 0

    constructor(taskName: String, taskDescription: String, taskEvaluationDay: Int, taskEvaluationTime: Long, categoryId: Int) {
        this.taskName = taskName
        this.taskDescription = taskDescription
        this.taskUpdateDate = getUpdateDate()
        this.taskEvaluationDay = taskEvaluationDay
        this.taskEvaluationTime = taskEvaluationTime
        this.categoryId = categoryId
    }

    constructor(){

    }

    fun getUpdateDate() : Long {
        return System.currentTimeMillis()
    }
}
