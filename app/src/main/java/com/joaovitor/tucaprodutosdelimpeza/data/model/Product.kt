package com.joaovitor.tucaprodutosdelimpeza.data.model

import java.io.Serializable

data class Product(
    val name: String = "",
    val price: String = "",
    val stock: Int = 0,
    val quantity: Int = 0
): Serializable