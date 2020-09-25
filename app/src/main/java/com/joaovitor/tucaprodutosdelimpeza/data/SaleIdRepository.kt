package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

class SaleIdRepository {

    private var colRef: CollectionReference =
        FirebaseFirestore.getInstance().collection("id_vendas")

    suspend fun generateSaleId(): Int{
        val result  = colRef.get().await()

        // Get all used IDs
        val ids: List<Int> = result.map { (it.get("venda") as Long).toInt() }

        for(i in 1000..99999) {
            if(!ids.contains(i)) return i
        }

        throw FirebaseFirestoreException("error finding an available sale id ",
            FirebaseFirestoreException.Code.UNKNOWN)
    }
}