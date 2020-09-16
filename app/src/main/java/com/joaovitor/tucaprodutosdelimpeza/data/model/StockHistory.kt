package com.joaovitor.tucaprodutosdelimpeza.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable
import java.util.Date

@IgnoreExtraProperties
class StockHistory(
    val saleId: Int = 0,
    val client: String = "",
    val date: Date = Date(),
    val quantity: Int = 0,
    val stockChange: Boolean?,
    val seller: String = "",
    val stockAdded: Boolean?
) : Serializable