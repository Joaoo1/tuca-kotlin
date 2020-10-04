package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.joaovitor.tucaprodutosdelimpeza.data.model.Address
import com.joaovitor.tucaprodutosdelimpeza.data.model.AddressType
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.data.util.DateRange
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import com.joaovitor.tucaprodutosdelimpeza.util.FormatDate
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class SaleRepository {

    private var colRef: CollectionReference =
        FirebaseFirestore.getInstance().collection("vendas")

    private var saleIdRepository = SaleIdRepository()

    suspend fun getSales(limit: Long? = 50): Result<List<Sale>> {
        return try {
            val querySnapshot = colRef
                .orderBy(Firestore.SALE_DATE, Query.Direction.DESCENDING)
                .limit(limit!!)
                .get()
                .await()

            val sales = querySnapshot.map {
                val sale = it.toObject(Sale::class.java)
                sale.id = it.id
                sale
            }

            Result.Success(sales)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getFilteredSales(
        dateRange: DateRange?,
        paid: Boolean? = null,
        address: Address? = null
    ): Result<List<Sale>> {
        return try {
            var querySnapshot = colRef
                .orderBy(Firestore.SALE_DATE, Query.Direction.DESCENDING)

            dateRange?.let {
                querySnapshot = querySnapshot
                    .whereGreaterThanOrEqualTo(Firestore.SALE_DATE, dateRange.startDate)

                querySnapshot = querySnapshot
                    .whereLessThan(Firestore.SALE_DATE, FormatDate.addOneDay(dateRange.endDate))
            }

            paid?.let {
                querySnapshot = querySnapshot
                    .whereEqualTo(Firestore.SALE_PAID, paid)
            }

            when(address?.type) {
                AddressType.STREET -> querySnapshot = querySnapshot
                    .whereEqualTo(Firestore.SALE_CLIENT_STREET, address.name)

                AddressType.NEIGHBORHOOD -> querySnapshot = querySnapshot
                    .whereEqualTo(Firestore.SALE_CLIENT_NEIGHBORHOOD, address.name)

                AddressType.CITY -> querySnapshot = querySnapshot
                    .whereEqualTo(Firestore.SALE_CLIENT_CITY, address.name)
            }

            val result = querySnapshot.get().await()

            val sales =  result.map {
                val sale = it.toObject(Sale::class.java)
                sale.id = it.id
                sale
            }

            Result.Success(sales)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getSalesByClient(clientId: String): Result<List<Sale>> {
        return try {
            val querySnapshot = colRef
                .whereEqualTo(Firestore.SALE_CLIENT_ID, clientId)
                .orderBy(Firestore.SALE_DATE, Query.Direction.DESCENDING)
                .get()
                .await()

            val clientSales =  querySnapshot.map {
                val sale = it.toObject(Sale::class.java)
                sale.id = it.id
                sale
            }

            Result.Success(clientSales)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun addSale(sale: Sale): Result<Void> {
        return try {
            // Getting a unused id for sale
            val resultAvailableId = saleIdRepository.generateSaleId()
            if(resultAvailableId is Result.Error) {
                return resultAvailableId
            }
            sale.saleId = (resultAvailableId as Result.Success).data!!

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