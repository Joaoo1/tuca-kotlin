package com.joaovitor.tucaprodutosdelimpeza.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class Client(

    @Exclude
    var id: String = "",

    @PropertyName("nome")
    @get:PropertyName("nome")
    @set:PropertyName("nome")
    var name: String = "",

    @PropertyName("rua")
    @get:PropertyName("rua")
    @set:PropertyName("rua")
    var street: String = "",

    @PropertyName("complemento")
    @get:PropertyName("complemento")
    @set:PropertyName("complemento")
    var complement: String = "",

    @PropertyName("bairro")
    @get:PropertyName("bairro")
    @set:PropertyName("bairro")
    var neighborhood: String = "",

    @PropertyName("cidade")
    @get:PropertyName("cidade")
    @set:PropertyName("cidade")
    var city: String = "",

    @PropertyName("telefone")
    @get:PropertyName("telefone")
    @set:PropertyName("telefone")
    var phone: String = ""

) : Serializable