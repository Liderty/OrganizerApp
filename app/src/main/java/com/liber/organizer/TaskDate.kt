package com.liber.organizer

import java.time.DayOfWeek
import java.util.*
import java.util.Calendar.*

class TaskDate {

    val TIME_OPERATOR = 60
    val MILLIS_OPERATOR = 100
    val DAY_IN_MILLIS = 100 * 60 * 60 * 24
    val DAYS_OF_WEEK = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    var hour: Int = 0
    var minute: Int = 0

    var milliseconds: Long = 0L

    constructor(dateTimeMillis: Long) {
        this.milliseconds = dateTimeMillis
    }

    constructor() {
        this.milliseconds = System.currentTimeMillis()
    }

    fun daysBetweenThisAndOther(otherDate: TaskDate) : Int {
        return ((otherDate.milliseconds - this.milliseconds)/DAY_IN_MILLIS).toInt()
    }

    fun getDayOfWeekIndex() : Int {
        var calendar = GregorianCalendar.getInstance()
        calendar.setTimeInMillis(this.milliseconds)

        var dayOfWeek = when(calendar.get(DAY_OF_WEEK)) {
            0 -> 6
            1 -> 0
            2 -> 1
            3 -> 2
            4 -> 3
            5 -> 4
            6 -> 5
            else -> 7
        }

        return dayOfWeek
    }

    fun getStringDayOf() : String {
        return getStringDayOf(getDayOfWeekIndex())
    }

    fun getStringDayOf(index: Int) : String {
        return DAYS_OF_WEEK[index]
    }
}