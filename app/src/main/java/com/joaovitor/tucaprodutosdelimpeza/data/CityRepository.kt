package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

class CityRepository {

    private var colRef: CollectionReference =
        FirebaseFirestore.getInstance().collection("cidades")

    suspend fun getCities(): List<String> {
        val querySnapshot = colRef.orderBy("nome_cidade").get().await()

        val cities: ArrayList<String> = arrayListOf()

        for (doc in querySnapshot){
            val city = doc.get("nome_cidade") as String
            cities.add(city)
        }
        return cities
    }

    suspend fun addCity(city: String): Result<Any> {
        return try {
            colRef.add(city).await()

            //product successful added
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }

}