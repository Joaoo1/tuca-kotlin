package com.joaovitor.tucaprodutosdelimpeza.data.model

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import kotlinx.coroutines.tasks.await
import java.io.Serializable
import java.math.BigDecimal
import java.util.Date

data class Sale(

    @get:Exclude var id: String = "",

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
    var total: String = "",

    @PropertyName("valorBruto")
    @get:PropertyName("valorBruto")
    @set:PropertyName("valorBruto")
    var grossValue: String = "",

    @PropertyName("desconto")
    @get:PropertyName("desconto")
    @set:PropertyName("desconto")
    var discount: String = "",

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
    var paid: Boolean = true,

    @PropertyName("idCliente")
    @get:PropertyName("idCliente")
    @set:PropertyName("idCliente")
    var clientId: String = "",

    @PropertyName("nomeCliente")
    @get:PropertyName("nomeCliente")
    @set:PropertyName("nomeCliente")
    var clientName: String = "",

    @PropertyName("enderecoCliente")
    @get:PropertyName("enderecoCliente")
    @set:PropertyName("enderecoCliente")
    var clientStreet: String = "",

    @PropertyName("bairroCliente")
    @get:PropertyName("bairroCliente")
    @set:PropertyName("bairroCliente")
    var clientNeighborhood: String = "",

    @PropertyName("cidadeCliente")
    @get:PropertyName("cidadeCliente")
    @set:PropertyName("cidadeCliente")
    var clientCity: String = "",

    @PropertyName("complementoCliente")
    @get:PropertyName("complementoCliente")
    @set:PropertyName("complementoCliente")
    var clientComplement: String = "",

    @PropertyName("telefone")
    @get:PropertyName("telefone")
    @set:PropertyName("telefone")
    var clientPhone: String = "",

    var products: MutableList<ProductSale> = mutableListOf(),
    var seller: String = "",
    var sellerUid: String = ""

) : Serializable {

    fun registerPayment(value: String){
        this.paidValue = BigDecimal(paidValue).plus(BigDecimal(value)).toString()
        this.toReceive = BigDecimal(total).minus(BigDecimal(paidValue)).toString()
    }

    fun finishSale() {
        this.paid = true
        this.paymentDate = Date()
        this.paidValue = this.total
        this.toReceive = "0.00"
    }

    fun toHashMap(): Map<String, Any?>{
        val data: MutableMap<String, Any?> = HashMap()
        data[Firestore.SALE_ID] = this.saleId
        data[Firestore.SALE_CLIENT_NAME] = this.clientName
        data[Firestore.SALE_CLIENT_PHONE] = this.clientPhone
        data[Firestore.SALE_CLIENT_STREET] = this.clientStreet
        data[Firestore.SALE_CLIENT_NEIGHBORHOOD] = this.clientNeighborhood
        data[Firestore.SALE_CLIENT_CITY] = this.clientCity
        data[Firestore.SALE_CLIENT_COMPLEMENT] = this.clientComplement
        data[Firestore.SALE_PAYMENT_DAY] = this.paymentDate
        data[Firestore.SALE_DISCOUNT] = this.discount
        data[Firestore.SALE_PAID] = this.paid
        data[Firestore.SALE_GROSS_VALUE] = this.grossValue
        data[Firestore.SALE_NET_VALUE] = this.total
        data[Firestore.SALE_PAID_VALUE] = this.paidValue
        data[Firestore.SALE_TO_RECEIVE] = this.toReceive
        data[Firestore.SALE_PRODUCTS] = this.products

        return data.toMap()
    }

    fun bindClient(client: Client) {
        this.clientName = client.name
        this.clientNeighborhood = client.neighborhood
        this.clientStreet = client.street
        this.clientCity = client.city
        this.clientComplement = client.complement
        this.clientId = client.id
        this.clientPhone = client.phone
    }
}