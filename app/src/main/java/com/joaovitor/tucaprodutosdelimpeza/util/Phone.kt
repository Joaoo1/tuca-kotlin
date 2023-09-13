package com.joaovitor.tucaprodutosdelimpeza.util

class Phone {
    companion object {
        fun getFormattedNumberToWhatsapp(phoneNumber: String): String {
            if(phoneNumber.isEmpty() || phoneNumber.length < 8 ) return ""

            if(phoneNumber.length <= 9) return "5549${phoneNumber}"

            if(phoneNumber.length <= 11) return "55${phoneNumber}"

            return phoneNumber
        }
    }
}