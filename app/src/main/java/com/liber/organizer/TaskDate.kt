package com.liber.organizer

import java.util.*
import java.util.Calendar.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.time.milliseconds

class TaskDate {

    val ADDITIONAL_DAY = 1
    val DAY_IN_MILLIS = 1000L * 60 * 60 * 24
    val DAYS_OF_WEEK =
        arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    var milliseconds: Long = 0L

    constructor(dateTimeMillis: Long) {
        this.milliseconds = dateTimeMillis
    }

    constructor() {
        this.milliseconds = System.currentTimeMillis()
    }

    fun getDaysBetweenThisAndOtherDate(otherDate: TaskDate): Int {
        return abs(((getDateWithoutTime(otherDate.milliseconds) - getDateWithoutTime(this.milliseconds)) / DAY_IN_MILLIS).toInt()) + ADDITIONAL_DAY
    }

    fun getDatesListBetweenThisAndOtherDate(otherDate: TaskDate): ArrayList<Long> {
        var datesList = arrayListOf<Long>()
        val daysBetween = this.getDaysBetweenThisAndOtherDate(otherDate)

        for (i in 0..daysBetween - 1) {
            datesList.add(getDateWithoutTime(this.milliseconds) - (i * DAY_IN_MILLIS))
        }

        return datesList
    }

    public fun getDaysListByIndexFromDatesList(index: Int, datesList: ArrayList<Long>): ArrayList<Long> {
        var daysList = arrayListOf<Long>()
        for (i in 0..datesList.size - 1) {
            if(TaskDate(datesList[i]).getDayOfWeekIndex() == index) {
                daysList.add(datesList[i])
            }
        }
        return daysList
    }

    fun getDayOfWeekIndex(): Int {
        var calendar = GregorianCalendar.getInstance()
        calendar.setTimeInMillis(this.milliseconds)

        var dayOfWeek = when (calendar.get(DAY_OF_WEEK)) {
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

    fun getDateWithoutTime(): Long {
        return getDateWithoutTime(this.milliseconds)
    }

    fun getDateWithoutTime(dateTimeMillis: Long): Long {
        var calendar = GregorianCalendar.getInstance()
        calendar.setTimeInMillis(dateTimeMillis)

        calendar.set(MILLISECOND, 0);
        calendar.set(SECOND, 0);
        calendar.set(MINUTE, 0);
        calendar.set(HOUR, 0);

        return calendar.timeInMillis;
    }

    fun getStringDayOf(): String {
        return getStringDayOf(getDayOfWeekIndex())
    }

    fun getStringDayOf(index: Int): String {
        return DAYS_OF_WEEK[index]
    }
}