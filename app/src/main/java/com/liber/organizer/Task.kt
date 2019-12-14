package com.liber.organizer

import com.liber.organizer.R
import java.io.Serializable

class Task : Serializable {

    var taskId: Int = 0
    var taskName: String = ""
    var taskDescription: String = ""
    var taskAvarage: Double = 0.0
    var taskIcon: Int = R.drawable.settings
    var categoryId: Int = 0

    constructor(taskName: String, taskDescription: String) {
        this.taskName = taskName
        this.taskDescription = taskDescription
    }

    constructor(taskName: String, taskDescription: String, categoryId: Int) {
        this.taskName = taskName
        this.taskDescription = taskDescription
        this.categoryId = categoryId
    }

    constructor(){

    }
}
