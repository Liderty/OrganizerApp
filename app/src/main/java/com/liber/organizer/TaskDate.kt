package com.liber.organizer

import java.util.*

class TaskDate {

    var taskYear: Int = 0
    var taskMonth: Int = 0
    var taskDay: Int = 0
    var taskHour: Int = 0
    var taskMinute: Int = 0

    constructor() {
        var currentDate = Calendar.getInstance()
        this.taskYear = currentDate.get
    }


}