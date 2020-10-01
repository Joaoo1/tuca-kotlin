package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.FirebaseFirestoreException
import com.joaovitor.tucaprodutosdelimpeza.data.model.ProductSold
import com.joaovitor.tucaprodutosdelimpeza.data.util.DateRange

class ReportRepository {
    suspend fun generateProductsSoldReport(dateRange: DateRange): Result<List<ProductSold>> {
        try{
            val sales = SaleRepository().getFilteredSales(dateRange)
            val products = ProductRepository().getProducts()
            val productsSold = products.map {
                var quantitySold = 0

                sales.forEach { sale ->
                    for (productSale in sale.products) {
                        if(productSale.parentId == it.id) quantitySold += productSale.quantity
                    }
                }

                ProductSold(it.name, quantitySold)
            }

            return Result.Success(productsSold)
        }catch (e: FirebaseFirestoreException) {
            return Result.Error(e)
        }
    }
}