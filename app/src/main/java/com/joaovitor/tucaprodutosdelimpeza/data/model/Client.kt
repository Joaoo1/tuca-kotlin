package com.joaovitor.tucaprodutosdelimpeza.data.model

import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class Client(

    val id: String = "",

    @PropertyName("nome")
    @get:PropertyName("nome")
    val name: String = "",

    @PropertyName("cidade")
    @get:PropertyName("cidade")
    val city: String = ""

) : Serializable