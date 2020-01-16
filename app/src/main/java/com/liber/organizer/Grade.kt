package com.liber.organizer

class Grade {

    var gradeId: Int = 0
    var gradeDate: Long = 0
    var gradeGrade: Int = 0
    var taskId: Int = 0

    constructor(gradeGrade: Int, taskId: Int) {
        this.gradeGrade = gradeGrade
        this.taskId = taskId
        this.gradeDate = System.currentTimeMillis()
    }

    constructor(taskId: Int, gradeDate: Long) {
        this.taskId = taskId
        this.gradeDate = gradeDate
    }

    constructor() {

    }
}
