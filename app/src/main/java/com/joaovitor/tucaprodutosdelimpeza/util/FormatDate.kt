package com.joaovitor.tucaprodutosdelimpeza.util

import java.text.SimpleDateFormat
import java.util.*

class FormatDate {

    companion object{
        private val locale: Locale = Locale("pt","BR")

        fun formatDateToString(date: Date): String {
            val f = SimpleDateFormat("dd/MM/yyyy HH:mm", locale)
            return f.format(date)
        }
    }
}
