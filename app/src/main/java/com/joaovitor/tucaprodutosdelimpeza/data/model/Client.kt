package com.joaovitor.tucaprodutosdelimpeza.data.model

import java.io.Serializable

data class Client(val id: String, val name: String, val city: String = "") : Serializable