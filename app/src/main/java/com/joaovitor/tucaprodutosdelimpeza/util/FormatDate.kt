package com.joaovitor.tucaprodutosdelimpeza.util

import android.util.StatsLog.logEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class FormatDate {

    companion object{
        private val locale: Locale = Locale("pt", "BR")

        fun formatDateToString(date: Date): String {
            val f = SimpleDateFormat("dd/MM/yyyy HH:mm", locale)
            f.timeZone = TimeZone.getTimeZone("UTC")
            return f.format(date)
        }

        fun formatLongToString(millis: Long): String {
            val date = Date(millis)
            return formatDateToString(date)
        }
    }
}
