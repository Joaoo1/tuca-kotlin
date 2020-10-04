package com.joaovitor.tucaprodutosdelimpeza.data

import com.joaovitor.tucaprodutosdelimpeza.data.model.ProductSold
import com.joaovitor.tucaprodutosdelimpeza.data.util.DateRange

class ReportRepository {
    suspend fun generateProductsSoldReport(dateRange: DateRange): Result<List<ProductSold>> {
        return try{
            val resultSales = SaleRepository().getFilteredSales(dateRange)
            val resultProducts = ProductRepository().getProducts()

            if (resultProducts is Result.Error) {
                return resultProducts
            }

            if (resultSales is Result.Error) {
                return resultSales
            }

            val productsSold = (resultProducts as Result.Success).data!!.map {
                var quantitySold = 0

                (resultSales as Result.Success).data?.forEach { sale ->
                    for (productSale in sale.products) {
                        if(productSale.parentId == it.id) quantitySold += productSale.quantity
                    }
                }

                ProductSold(it.name, quantitySold)
            }

            Result.Success(productsSold)
        }catch (e: Exception) {
            Result.Error(e)
        }
    }
}