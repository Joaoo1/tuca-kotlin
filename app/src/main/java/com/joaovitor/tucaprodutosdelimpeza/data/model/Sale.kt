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
    @set:PropertyName("idVenda")
    var saleId: Int = 0,

    @PropertyName("dataVenda")
    @get:PropertyName("dataVenda")
    @set:PropertyName("dataVenda")
    var saleDate: Date = Date(),

    @PropertyName("dataPagamento")
    @get:PropertyName("dataPagamento")
    @set:PropertyName("dataPagamento")
    var paymentDate: Date? = null,

    @PropertyName("valorLiquido")
    @get:PropertyName("valorLiquido")
    @set:PropertyName("valorLiquido")
    var total: String? = "",

    @PropertyName("valorBruto")
    @get:PropertyName("valorBruto")
    @set:PropertyName("valorBruto")
    var grossValue: String? = "",

    @PropertyName("desconto")
    @get:PropertyName("desconto")
    @set:PropertyName("desconto")
    var discount: String? = "",

    @PropertyName("valorAReceber")
    @get:PropertyName("valorAReceber")
    @set:PropertyName("valorAReceber")
    var toReceive: String = "",

    @PropertyName("valorPago")
    @get:PropertyName("valorPago")
    @set:PropertyName("valorPago")
    var paidValue: String = "",

    @PropertyName("pago")
    @get:PropertyName("pago")
    @set:PropertyName("pago")
    var paid: Boolean? = true,

    @PropertyName("idCliente")
    @get:PropertyName("idCliente")
    @set:PropertyName("idCliente")
    var clientId: String? = "",

    @PropertyName("nomeCliente")
    @get:PropertyName("nomeCliente")
    @set:PropertyName("nomeCliente")
    var clientName: String? = "",

    @PropertyName("enderecoCliente")
    @get:PropertyName("enderecoCliente")
    @set:PropertyName("enderecoCliente")
    var clientStreet: String? = "",

    @PropertyName("bairroCliente")
    @get:PropertyName("bairroCliente")
    @set:PropertyName("bairroCliente")
    var clientNeighborhood: String? = "",

    @PropertyName("cidadeCliente")
    @get:PropertyName("cidadeCliente")
    @set:PropertyName("cidadeCliente")
    var clientCity: String? = "",

    @PropertyName("complementoCliente")
    @get:PropertyName("complementoCliente")
    @set:PropertyName("complementoCliente")
    var clientComplement: String? = "",

    @PropertyName("telefone")
    @get:PropertyName("telefone")
    @set:PropertyName("telefone")
    var clientPhone: String? = "",

    var products: List<ProductSale> = listOf(),
    var seller: String? = "",
    var sellerUid: String? = ""

) : Serializable