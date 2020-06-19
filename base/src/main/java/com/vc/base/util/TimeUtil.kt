package com.vc.base.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Long.getTimeStr(formatStr: String,zoneId:String="GNT"): String {
    val sdf = SimpleDateFormat(formatStr)
    sdf.timeZone = TimeZone.getTimeZone(zoneId)
    return sdf.format(this)
}

@SuppressLint("SimpleDateFormat")
fun Long.yyyyMMddHHmmss(): String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this)

fun Long.toJavaTime(): Long = this * 1000L

@SuppressLint("SimpleDateFormat")
fun String.getTimeStr(formatStr: String): String {
    val sdf = SimpleDateFormat(formatStr)
    sdf.timeZone = TimeZone.getTimeZone(this)
    return sdf.format(Date())
}


