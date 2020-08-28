package com.joaovitor.tucaprodutosdelimpeza.data.model

import java.io.Serializable
import java.util.Date

class StockHistory(val sale: Int, val client: String, val date: Date) : Serializable