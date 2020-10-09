package com.joaovitor.tucaprodutosdelimpeza.data.model

import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable
import java.util.Date

@IgnoreExtraProperties
data class StockMovement(
    var product: ProductSale? = null,
    var saleId: Int? = null,
    var isStockChange: Boolean? = null,
    var seller: String = "",
    var quantity: Int = product?.quantity?: 0,
    var newStock: Int? = 0,
    var date: Date = Date()) : Serializable