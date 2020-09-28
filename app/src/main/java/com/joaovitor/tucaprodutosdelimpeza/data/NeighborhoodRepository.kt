package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

class NeighborhoodRepository {

    private var colRef: CollectionReference =
        FirebaseFirestore.getInstance().collection("bairros")

    suspend fun getNeighborhoods(): List<String> {
        val querySnapshot = colRef.orderBy("nome_bairro").get().await()

        val neighborhoods: ArrayList<String> = arrayListOf()

        for (doc in querySnapshot){
            val neighborhood = doc.get("nome_bairro") as String
            neighborhoods.add(neighborhood)
        }
        return neighborhoods
    }

    suspend fun addNeighborhood(neighborhood: String): Result<Any> {
        return try {
            colRef.add(neighborhood).await()

            //product successful added
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }
}