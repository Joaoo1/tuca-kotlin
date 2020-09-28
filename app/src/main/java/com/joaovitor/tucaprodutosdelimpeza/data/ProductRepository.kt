package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.StockHistory
import kotlinx.coroutines.tasks.await

class ProductRepository {

    private var colRef: CollectionReference =
        FirebaseFirestore.getInstance().collection("produtos")

    suspend fun getProducts(): List<Product> {
        val querySnapshot = colRef.orderBy("nome", Query.Direction.ASCENDING).get().await()

        return querySnapshot.map {
            val product = it.toObject(Product::class.java)
            product.id = it.id
            product
        }
    }

    suspend fun getStockHistories(productId: String): List<StockHistory> {
        return colRef
            .document(productId)
            .collection("stockHistory")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .await()
            .toObjects(StockHistory::class.java)
    }

    suspend fun addProduct(product: Product): Result<Any> {
        return try {
            colRef.add(product).await()

            //product successful added
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }
}