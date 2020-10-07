package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class ProductRepository {

    private var colRef: CollectionReference =
        FirebaseFirestore.getInstance().collection("produtos")

    suspend fun getProducts(): Result<List<Product>> {
        return try {
            val querySnapshot = colRef.orderBy("nome", Query.Direction.ASCENDING).get().await()

            val products = querySnapshot.map {
                val product = it.toObject(Product::class.java)
                product.id = it.id
                product
            }

            Result.Success(products)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getProductsRefs(): Result<QuerySnapshot> {
        return try {
            val querySnapshot = colRef.orderBy("nome", Query.Direction.ASCENDING).get().await()

            Result.Success(querySnapshot)
        } catch (e: Exception) {
            Result.Error(e)
        }
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

    suspend fun editProduct(product: Product): Result<Any> {
        return try {
            colRef.document(product.id).set(product).await()

            //product successful added
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }

    suspend fun deleteProduct(productId: String): Result<Any> {
        return try {
            colRef.document(productId).delete().await()

            //product successful added
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }

}
