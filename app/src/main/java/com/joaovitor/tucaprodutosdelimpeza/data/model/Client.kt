package com.joaovitor.tucaprodutosdelimpeza.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import java.io.Serializable

data class Client(

    @get:Exclude var id: String = "",

    @PropertyName("nome")
    @get:PropertyName("nome")
    @set:PropertyName("nome")
    var name: String = "",

    @PropertyName("endereco")
    @get:PropertyName("endereco")
    @set:PropertyName("endereco")
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

) : Serializable {

    fun bind(client: Client) {
        this.id = client.id
        this.name = client.name
        this.street = client.street
        this.complement = client.complement
        this.neighborhood = client.neighborhood
        this.city = client.city
        this.phone = client.phone
    }

    fun toSaleHashMap(): Map<String, Any?> {
        val data: MutableMap<String, Any?> = HashMap()
        data[Firestore.SALE_CLIENT_NAME] = this.name
        data[Firestore.SALE_CLIENT_PHONE] = this.phone
        data[Firestore.SALE_CLIENT_STREET] = this.street
        data[Firestore.SALE_CLIENT_NEIGHBORHOOD] = this.neighborhood
        data[Firestore.SALE_CLIENT_CITY] = this.city
        data[Firestore.SALE_CLIENT_COMPLEMENT] = this.complement

        return data.toMap()
    }
}