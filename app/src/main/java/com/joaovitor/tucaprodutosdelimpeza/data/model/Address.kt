package com.joaovitor.tucaprodutosdelimpeza.data.model

import java.io.Serializable

interface Address: Serializable {
    var id: String
    var name: String
    var type: AddressType?
}

enum class AddressType(val value: String) {
    STREET("rua"),
    NEIGHBORHOOD("bairro"),
    CITY("cidade"),
}
