package com.zero.healthmonitoring

import java.text.SimpleDateFormat
import java.util.*

fun main(args: Array<String>) {
    val sdf = SimpleDateFormat("yyyy-MM")
    println(getDaysOfMonth(sdf.parse("2015-1")))
}

fun getDaysOfMonth(date: Date): Int {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
}