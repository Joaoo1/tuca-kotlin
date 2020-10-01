package com.joaovitor.tucaprodutosdelimpeza.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class City(

    @get:Exclude override var id: String = "",

    @PropertyName("nome_cidade")
    @get:PropertyName("nome_cidade")
    @set:PropertyName("nome_cidade")
    override var name: String = "",

    @get:Exclude override var type: AddressType? = null

): Serializable, Address