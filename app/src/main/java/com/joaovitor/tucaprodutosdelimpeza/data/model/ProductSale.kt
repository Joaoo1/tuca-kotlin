package com.joaovitor.tucaprodutosdelimpeza.data.model

import com.google.firebase.firestore.PropertyName
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

data class ProductSale(

    @PropertyName("nome")
    @get:PropertyName("nome")
    val name: String = "",

    @PropertyName("preco")
    @get:PropertyName("preco")
    val price: String = "",

    @PropertyName("quantidade")
    @get:PropertyName("quantidade")
    val quantity: Int = 0,

    @PropertyName("add_date")
    @get:PropertyName("add_date")
    val addDate: Date = Date(),

    val parentId: String = ""
): Serializable {
    val total: String
        get() {
            val total = BigDecimal(this.price).multiply(BigDecimal(this.quantity))
            return total.toString()
        }
}