package com.joaovitor.tucaprodutosdelimpeza.data.model

import java.util.Date

data class GeneralInfo(
    var clients: Int = 0,
    var products: Int = 0,
    var sales: Int = 0,
    var updatedAt: Date? = null
)