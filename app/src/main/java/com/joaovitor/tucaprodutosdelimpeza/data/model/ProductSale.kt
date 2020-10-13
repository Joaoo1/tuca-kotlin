package com.joaovitor.tucaprodutosdelimpeza.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import java.io.Serializable
import java.math.BigDecimal
import java.util.Date

data class ProductSale(

    @PropertyName("nome")
    @get:PropertyName("nome")
    val name: String = "",

    @PropertyName("preco")
    @get:PropertyName("preco")
    val price: String = "",

    @PropertyName("quantidade")
    @get:PropertyName("quantidade")
    var quantity: Int = 0,

    @PropertyName("add_date")
    @get:PropertyName("add_date")
    val addDate: Date? = Date(),

    val parentId: String = "",

    @get:Exclude
    var isPostAdded: Boolean? = null,

    @get:Exclude
    var manageStock: Boolean = false
): Serializable {
    @get:Exclude
    val total: String
        get() {
            val total = BigDecimal(this.price).multiply(BigDecimal(this.quantity))
            return total.toString()
        }
}