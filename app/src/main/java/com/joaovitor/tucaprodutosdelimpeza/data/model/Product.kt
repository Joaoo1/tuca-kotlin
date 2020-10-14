package com.joaovitor.tucaprodutosdelimpeza.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
data class Product(

    @get:Exclude var id: String = "",

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
): Serializable {

    fun toProductSale(quantity: Int): ProductSale{
        return ProductSale(
            name = this.name,
            price = this.price,
            quantity = quantity,
            addDate = Date(),
            parentId = this.id,
            manageStock = this.manageStock)
    }

    fun bind(product: Product) {
        this.id = product.id
        this.name = product.name
        this.price = product.price
        this.manageStock = product.manageStock
        this.stock = product.stock
    }
}