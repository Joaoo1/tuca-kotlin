package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
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

    fun setIdAsUsed(saleId: Int) {
        val data: MutableMap<String, Int> = HashMap()
        data["venda"] = saleId
        FirebaseFirestore.getInstance().collection(Firestore.COL_SALES_ID).add(data)
    }
}