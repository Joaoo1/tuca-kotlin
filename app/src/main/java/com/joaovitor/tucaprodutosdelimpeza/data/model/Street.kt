package com.joaovitor.tucaprodutosdelimpeza.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class Street(

    @get:Exclude override var id: String = "",

    @PropertyName("nome_rua")
    @get:PropertyName("nome_rua")
    @set:PropertyName("nome_rua")
    override var name: String = "",

    @get:Exclude override var type: AddressType? = null

): Serializable, Address