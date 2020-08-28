package com.joaovitor.tucaprodutosdelimpeza.data.model

import java.io.Serializable

data class Product(
    val name: String,
    val price: String,
    val stock: Number,
    val quantity: Number
): Serializable