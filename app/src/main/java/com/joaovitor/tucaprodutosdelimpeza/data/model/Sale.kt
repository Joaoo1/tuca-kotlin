package com.joaovitor.tucaprodutosdelimpeza.data.model

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import java.io.Serializable
import java.util.Date

@IgnoreExtraProperties
data class Sale(
    var id: String = "",

    @PropertyName("idVenda")
    @get:PropertyName("idVenda")
    val saleId: Int = 0,

    @PropertyName("dataVenda")
    @get:PropertyName("dataVenda")
    val saleDate: Date? = null,

    @PropertyName("dataPagamento")
    @get:PropertyName("dataPagamento")
    val paymentDate: Date? = null,

    @PropertyName("valorLiquido")
    @get:PropertyName("valorLiquido")
    val total: String? = "",

    @PropertyName("valorBruto")
    @get:PropertyName("valorBruto")
    val grossValue: String? = "",

    @PropertyName("desconto")
    @get:PropertyName("desconto")
    val discount: String? = "",

    @get:PropertyName("valorAReceber")
    @PropertyName("valorAReceber")
    val toReceive: String = "",

    @PropertyName("pago")
    @get:PropertyName("pago")
    val paid: Boolean? = true,

    @PropertyName("idCliente")
    @get:PropertyName("idCliente")
    val clientId: String? = "",

    @PropertyName("nomeCliente")
    @get:PropertyName("nomeCliente")
    val clientName: String? = "",

    @PropertyName("enderecoCliente")
    @get:PropertyName("enderecoCliente")
    val clientStreet: String? = "",

    @PropertyName("bairroCliente")
    @get:PropertyName("bairroCliente")
    val clientNeighborhood: String? = "",

    @PropertyName("cidadeCliente")
    @get:PropertyName("cidadeCliente")
    val clientCity: String? = "",

    @PropertyName("complementoCliente")
    @get:PropertyName("complementoCliente")
    val clientComplement: String? = "",

    val products: ArrayList<Product>? = null,
    val seller: String? = "",
    val sellerUid: String? = ""

) : Serializable