package com.joaovitor.tucaprodutosdelimpeza.data.model

import java.io.Serializable

data class User (
    var uid: String? = "",
    var name: String? = "",
    var email: String? = "",
): Serializable