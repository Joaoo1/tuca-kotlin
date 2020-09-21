package com.joaovitor.tucaprodutosdelimpeza.util

import java.text.SimpleDateFormat
import java.util.*

object FormatDate {

    private val mLocale: Locale = Locale("pt", "BR")

    fun formatDateToString(date: Date, locale: Locale? = mLocale): String {
        val f = SimpleDateFormat("dd/MM/yyyy HH:mm", locale)
        return f.format(date)
        }

    fun add3HoursToTimestamp(millis: Long): Long {
        return millis + 10800000
    }
}
