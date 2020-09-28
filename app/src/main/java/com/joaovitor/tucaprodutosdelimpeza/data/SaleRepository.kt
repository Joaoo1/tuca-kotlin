package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.data.util.DateRange
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import kotlinx.coroutines.tasks.await

class SaleRepository {

    private var colRef: CollectionReference =
        FirebaseFirestore.getInstance().collection("vendas")

    private var saleIdRepository = SaleIdRepository()

    suspend fun getSales(limit: Long? = 50): List<Sale> {
        val querySnapshot = colRef
            .orderBy(Firestore.SALE_DATE, Query.Direction.DESCENDING)
            .limit(limit!!)
            .get()
            .await()

        return querySnapshot.map {
            val sale = it.toObject(Sale::class.java)
            sale.id = it.id
            sale
        }
    }

    suspend fun getFilteredSales(dateRange: DateRange?, paid: Boolean?): List<Sale> {
        var querySnapshot = colRef
            .orderBy(Firestore.SALE_DATE, Query.Direction.DESCENDING)

        dateRange?.let {
            querySnapshot = querySnapshot
                .whereGreaterThanOrEqualTo(Firestore.SALE_DATE, dateRange.startDate)

            querySnapshot = querySnapshot
                .whereLessThanOrEqualTo(Firestore.SALE_DATE, dateRange.endDate)
        }

        paid?.let {
             querySnapshot = querySnapshot
                 .whereEqualTo(Firestore.SALE_PAID, paid)
        }

        val result = querySnapshot.get().await()

        return result.toObjects(Sale::class.java)
    }

    suspend fun getSalesByClient(clientId: String): List<Sale> {
        val querySnapshot = colRef
            .whereEqualTo(Firestore.SALE_CLIENT_ID, clientId)
            .orderBy(Firestore.SALE_DATE, Query.Direction.DESCENDING)
            .get()
            .await()

        return querySnapshot.map {
            val sale = it.toObject(Sale::class.java)
            sale.id = it.id
            sale
        }
    }

    suspend fun addSale(sale: Sale): Result<Void> {
        return try {
            // Getting a unused id for sale
            sale.saleId = saleIdRepository.generateSaleId()

            colRef.add(sale).await()

            //Sale successful added
            SaleIdRepository().setIdAsUsed(sale.saleId)
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }

    suspend fun editSale(sale: Sale): Result<Void> {
        return try {
            colRef.document(sale.id).update(sale.toHashMap()).await()

            Result.Success(null)
        } catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }

    suspend fun deleteSale(id: String): Result<Void>{
        return try {
            colRef.document(id).delete().await()

            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }
}