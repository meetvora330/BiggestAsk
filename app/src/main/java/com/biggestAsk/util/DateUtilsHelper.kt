package com.biggestAsk.util

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


const val REVIEW_SERVER_DATE = "yyyy-MM-dd hh:mm:ss"
const val REVIEW_CONVERTED_DATE = "MMMM dd yyyy"
const val MONTH_YEAR_DATE = "dd MMM "
const val DATE_FORMAT = "EEE dd MMM yyyy"


@SuppressLint("SimpleDateFormat")
fun getConvertTime(getTime: String?): String? {
    var formattedDate: String? = null
    try {
        val inputFormat = SimpleDateFormat(REVIEW_SERVER_DATE)
        val outputFormat = SimpleDateFormat(REVIEW_CONVERTED_DATE)
        val date = inputFormat.parse(getTime!!)
        formattedDate = outputFormat.format(date!!)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return formattedDate
}

fun getCurrentDate(): Date? {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 2)
    return calendar.time
}

fun get45DaysDate(): Date? {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 47)
    return calendar.time
}

fun convertDate(date: Date): String? {
    return SimpleDateFormat(MONTH_YEAR_DATE, Locale.ENGLISH).format(date.time)
}

fun convertDateFormat(date: Date): String? {
    return SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(date.time)
}

fun convertDateYearFormat(date: Date): String? {
    return SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(date.time)
}

@SuppressLint("SimpleDateFormat")
fun changeDateFormat(date: String): String? {
    val format1 = SimpleDateFormat("yyyy-MM-dd")
    val format2 = SimpleDateFormat("dd.MM.yyyy")
    var mDate: Date? = null
    try {
        mDate = format1.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return format2.format(mDate!!)
}

fun changeOrderDateFormat(mDate: String?): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val newFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    var utcDate: Date? = Date()
    try {
        utcDate = formatter.parse(mDate!!)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return newFormat.format(utcDate!!)
}

fun convertScheduleDate(date: String, isDay: Boolean): String {
    val convertedDate: Date
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("en"))
    val dayFormatWeek = SimpleDateFormat("EEEE", Locale("en"))
    val dateFormatWeek = SimpleDateFormat("dd MMM yyyy", Locale("en"))

    convertedDate = dateFormat.parse(date)!!
    return if (isDay) {
        dayFormatWeek.format(convertedDate)
    } else {
        dateFormatWeek.format(convertedDate)
    }
}