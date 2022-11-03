package com.biggestAsk.util

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SimpleDateFormat")
fun changeDateFormat(date: String): String? {
    val format1 = SimpleDateFormat(Constants.DATE_FORMAT_LOCAL)
    val format2 = SimpleDateFormat(Constants.DATE_FORMAT_UTC)
    var mDate: Date? = null
    try {
        mDate = format1.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return format2.format(mDate!!)
}

@SuppressLint("SimpleDateFormat")
fun changeLocalFormat(date: String,input_date_format:String,output_date_format:String): String? {
    val format1 = SimpleDateFormat(input_date_format)
    val format2 = SimpleDateFormat(output_date_format)
    var mDate: Date? = null
    try {
        mDate = format1.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return format2.format(mDate!!)
}


@SuppressLint("SimpleDateFormat")
fun changeLocalDateFormat(date: String):String?{
    val format1 = SimpleDateFormat(Constants.DATE_FORMAT_LOCAL)
    val format2 = SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT)
    var mDate: Date? = null
    try {
        mDate = format1.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return format2.format(mDate!!)
}

@SuppressLint("SimpleDateFormat")
fun changeLocalTimeFormat(date: String):String?{
    val format1 = SimpleDateFormat(Constants.DATE_FORMAT_LOCAL)
    val format2 = SimpleDateFormat(Constants.SIMPLE_TIME_FORMAT)
    var mDate: Date? = null
    try {
        mDate = format1.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return format2.format(mDate!!)
}