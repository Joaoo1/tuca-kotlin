package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import kotlinx.coroutines.tasks.await

class SaleRepository {

    private var colRef: CollectionReference =
        FirebaseFirestore.getInstance().collection("vendas")

    suspend fun getSales(): List<Sale> {
        val querySnapshot = colRef.limit(50).get().await()

        return querySnapshot.toObjects(Sale::class.java)
    }
}