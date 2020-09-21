package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class StreetRepository {

    private var colRef: CollectionReference =
        FirebaseFirestore.getInstance().collection("ruas")

    suspend fun getStreets(): List<String> {
        val querySnapshot = colRef.orderBy("nome_rua").get().await()

        val streets: ArrayList<String> = arrayListOf()

        for (doc in querySnapshot){
            val street = doc.get("nome_rua") as String
            streets.add(street)
        }
        return streets
    }
}