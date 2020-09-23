package com.joaovitor.tucaprodutosdelimpeza.util

import androidx.databinding.InverseMethod

object Converter {
    @InverseMethod("stringToInt")
    @JvmStatic fun intToString(value: Int): String {
        if(value == 0) return ""
        return value.toString()
    }

    @JvmStatic fun stringToInt(value: String): Int {
        if(value.isEmpty()) return 0
        return Integer.parseInt(value)
    }
}