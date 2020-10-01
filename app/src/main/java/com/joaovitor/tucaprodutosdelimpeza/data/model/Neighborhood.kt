package com.joaovitor.tucaprodutosdelimpeza.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class Neighborhood(

    @get:Exclude override var id: String = "",

    @PropertyName("nome_bairro")
    @get:PropertyName("nome_bairro")
    @set:PropertyName("nome_bairro")
    override var name: String = "",

    @get:Exclude override var type: AddressType? = null

): Serializable, Address