package com.joaovitor.tucaprodutosdelimpeza.data.model

import java.io.Serializable
import java.util.Date

data class Sale(
    val saleId: Int,
    val client: Client,
    val saleDate: Date,
    val total: String
    ) : Serializable