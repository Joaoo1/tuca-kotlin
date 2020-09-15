package com.joaovitor.tucaprodutosdelimpeza.data.model

import com.google.firebase.firestore.PropertyName
import java.io.Serializable
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
    val addDate: Date? = null,

    val manageStock: Boolean = false,

    val parentId: String = ""
): Serializable