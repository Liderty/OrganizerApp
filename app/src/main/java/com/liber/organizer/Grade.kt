package com.liber.organizer

import java.io.Serializable

class Grade : Serializable {

    var gradeId: Int = 0
    var gradeDate: Long = 0
    var gradeGrade: Int = 0
    var taskId: Int = 0

    constructor(taskId: Int, gradeDate: Long) {
        this.taskId = taskId
        this.gradeDate = gradeDate
    }

    constructor() {

    }
}
