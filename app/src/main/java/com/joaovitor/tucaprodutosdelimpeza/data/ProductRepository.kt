package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import kotlinx.coroutines.tasks.await

class ProductRepository {

    private var colRef: CollectionReference =
        FirebaseFirestore.getInstance().collection("produtos")

    suspend fun getProducts(): List<Product> {
        val querySnapshot = colRef.limit(50).get().await()

        return querySnapshot.toObjects(Product::class.java)
    }
}