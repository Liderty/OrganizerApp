package com.liber.organizer

import java.util.*
import java.util.Calendar.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class TaskDate {

    val ADDITIONAL_DAY = 1
    val HOUR_IN_MILLIS = 60 * 60 * 1000
    val DAY_IN_MILLIS = HOUR_IN_MILLIS * 24L
    val DAYS_OF_WEEK =
        arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    val TIME_ZONE = "UTC+1"

    var milliseconds: Long = 0L

    constructor(dateTimeMillis: Long) {
        this.milliseconds = dateTimeMillis
    }

    constructor() {
        this.milliseconds = System.currentTimeMillis() + HOUR_IN_MILLIS
    }

    constructor(dateMillis: Long, timeMillis: Long) {
        this.milliseconds = this.getDateWithoutTime(dateMillis) + timeMillis
    }

    fun getDaysBetweenThisAndOtherDate(otherDate: TaskDate): Int {
        return abs(((getDateWithoutTime(otherDate.milliseconds) - getDateWithoutTime(this.milliseconds)) / DAY_IN_MILLIS).toInt()) + ADDITIONAL_DAY
    }

    fun TaskDate.add(addedMilliseconds: Long) {
        this.milliseconds = this.milliseconds + addedMilliseconds
    }

    fun getDatesListBetweenThisAndOtherDate(otherDate: TaskDate): ArrayList<Long> {
        val datesList = arrayListOf<Long>()
        val daysBetween = this.getDaysBetweenThisAndOtherDate(otherDate)

        for (i in 0..daysBetween - 1) {
            datesList.add(getDateWithoutTime(this.milliseconds) - (i * DAY_IN_MILLIS))
        }

        return datesList
    }

    fun getDaysListByIndexFromDatesList(dayIndex: Int, datesList: ArrayList<Long>): ArrayList<Long> {
        val daysList = arrayListOf<Long>()
        for (i in 0..datesList.size - 1) {
            if(TaskDate(datesList[i]).getDayOfWeekIndex() == dayIndex) {
                daysList.add(datesList[i])
            }
        }
        return daysList
    }

    fun getHour() : Int {
        val calendar = getInstance(TimeZone.getTimeZone(TIME_ZONE))
        calendar.setTimeInMillis(this.milliseconds)
        return calendar.get(HOUR_OF_DAY)
    }

    fun getMinute() : Int {
        val calendar = getInstance(TimeZone.getTimeZone(TIME_ZONE))
        calendar.setTimeInMillis(this.milliseconds)
        return calendar.get(MINUTE)
    }

    fun getDayOfWeekIndex(): Int {
        val calendar = getInstance(TimeZone.getTimeZone(TIME_ZONE))
        calendar.setTimeInMillis(this.milliseconds)

        val dayOfWeek = when (calendar.get(DAY_OF_WEEK)) {
            1 -> 6
            2 -> 0
            3 -> 1
            4 -> 2
            5 -> 3
            6 -> 4
            7 -> 5
            else -> 7
        }
        return dayOfWeek
    }

    fun getDateWithoutTime(): Long {
        return getDateWithoutTime(this.milliseconds)
    }

    fun getDateWithoutTime(dateTimeMillis: Long): Long {
        var calendar = getInstance(TimeZone.getTimeZone(TIME_ZONE))
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

    fun getStringDate() : String {
        val calendar = getInstance(TimeZone.getTimeZone(TIME_ZONE))
        calendar.setTimeInMillis(milliseconds)
        val dateString = "${calendar.get(DAY_OF_MONTH)}/${calendar.get(MONTH) + 1}/${calendar.get(YEAR)}"
        return dateString
    }

    fun getTimeWithoutDate() : Long {
        val hoursInMillis = this.getHour() * 60L * 60 * 1000
        val minutesInMillis = this.getMinute() * 60L * 1000

        return hoursInMillis + minutesInMillis
    }

    fun getNextDayAndTime(targetDayIndex: Int, targetTimeInMillis: Long) : Long {
        val today = this.getDayOfWeekIndex()

        val daysDifference: Int
        var additionalDays = 0L

        if(targetDayIndex > 6) {
            if(targetTimeInMillis <= this.getTimeWithoutDate()) {
                additionalDays = DAY_IN_MILLIS
            }
        } else {
            if(targetDayIndex > today) {
                daysDifference = abs(targetDayIndex - today)
                additionalDays = (DAY_IN_MILLIS * daysDifference)

            } else if(targetDayIndex < today) {
                daysDifference = abs(DAYS_OF_WEEK.size + targetDayIndex - today)
                additionalDays = (DAY_IN_MILLIS * daysDifference)

            } else {
                if(targetTimeInMillis < this.getTimeWithoutDate()) {
                    daysDifference = DAYS_OF_WEEK.size
                    additionalDays = (DAY_IN_MILLIS * daysDifference)
                }
            }
        }

        this.milliseconds = this.getDateWithoutTime()
        this.add(targetTimeInMillis)
        this.add(additionalDays)

        return this.milliseconds
    }
}
