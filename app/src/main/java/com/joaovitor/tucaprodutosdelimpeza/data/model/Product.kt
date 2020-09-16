package com.joaovitor.tucaprodutosdelimpeza.data.model

import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class Product(
    @PropertyName("nome")
    @get:PropertyName("nome")
    @set:PropertyName("nome")
    var name: String = "",

    @PropertyName("preco")
    @get:PropertyName("preco")
    @set:PropertyName("preco")
    var price: String = "",

    @PropertyName("currentStock")
    @get:PropertyName("currentStock")
    @set:PropertyName("currentStock")
    var stock: Int = 0,

    var manageStock: Boolean = false
): Serializable