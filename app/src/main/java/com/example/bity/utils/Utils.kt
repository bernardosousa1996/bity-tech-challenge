package com.example.bity.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun weiToEth(wei: String): String {
    return try {
        val weiValue = BigDecimal(wei)
        val ethValue = weiValue.divide(BigDecimal("1000000000000000000"))
        ethValue.setScale(5, RoundingMode.HALF_UP).toPlainString()

    } catch (e: Exception) {
        "Invalid value"
    }
}

fun formatTimestamp(timestamp: String): String {
    return try {
        val date = Date(timestamp.toLong() * 1000)
        val format = SimpleDateFormat("MMM-dd-yyyy hh:mm:ss a", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("UTC")
        format.format(date)
    } catch (e: Exception) {
        "Invalid Date"
    }

}