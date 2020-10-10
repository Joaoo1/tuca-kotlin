package com.joaovitor.tucaprodutosdelimpeza.data.util

object Firestore {
    const val USER_NAME = "name"

    const val COL_SALES = "vendas"
    const val SALE_ID = "idVenda"
    const val SALE_PRODUCTS = "products"
    const val SALE_CLIENT_ID = "idCliente"
    const val SALE_CLIENT_NAME = "nomeCliente"
    const val SALE_CLIENT_PHONE = "telefone"
    const val SALE_CLIENT_STREET = "enderecoCliente"
    const val SALE_CLIENT_NEIGHBORHOOD = "bairroCliente"
    const val SALE_CLIENT_CITY = "cidadeCliente"
    const val SALE_CLIENT_COMPLEMENT = "complementoCliente"
    const val SALE_PAYMENT_DAY = "dataPagamento"
    const val SALE_DATE = "dataVenda"
    const val SALE_DISCOUNT = "desconto"
    const val SALE_PAID = "pago"
    const val SALE_GROSS_VALUE = "valorBruto"
    const val SALE_NET_VALUE = "valorLiquido"
    const val SALE_PAID_VALUE = "valorPago"
    const val SALE_TO_RECEIVE = "valorAReceber"
    const val SALE_SELLER = "seller"
    const val SALE_SELLER_UID = "sellerUid"

    const val COL_PRODUCTS = "produtos"
    const val PRODUCT_CURRENT_STOCK = "currentStock"
    const val PRODUCT_MANAGE_STOCK = "manageStock"
    const val SUBCOL_STOCK_MOVEMENT = "stockMovement"
    const val STOCK_MOVEMENT_DATE = "date"

    const val COL_CLIENTS = "clientes"
    const val CLIENT_NAME = "nome"
    const val CLIENT_PHONE = "telefone"
    const val CLIENT_STREET = "endereco"
    const val CLIENT_NEIGHBORHOOD = "bairro"
    const val CLIENT_CITY = "cidade"
    const val CLIENT_COMPLEMENT = "complemento"

    const val COL_STREETS = "ruas"
    const val STREET_NAME = "nome_rua"

    const val COL_NEIGHBORHOODS = "bairros"
    const val NEIGHBORHOOD_NAME = "nome_bairro"

    const val COL_CITIES = "cidades"
    const val CITY_NAME = "nome_cidade"

    const val COL_SALES_ID = "id_vendas"

}
