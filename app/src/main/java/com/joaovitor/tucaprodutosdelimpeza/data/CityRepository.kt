package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.joaovitor.tucaprodutosdelimpeza.data.model.City
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import kotlinx.coroutines.tasks.await

class CityRepository {

    private var colRef: CollectionReference =
        FirebaseFirestore.getInstance().collection(Firestore.COL_CITIES)

    suspend fun getCities(): List<City> {
        val querySnapshot = colRef.orderBy(Firestore.CITY_NAME).get().await()

        val cities: MutableList<City> = mutableListOf()

        for (doc in querySnapshot){
            val city = doc.toObject(City::class.java)
            city.id = doc.id
            cities.add(city)
        }
        return cities
    }

    suspend fun addCity(city: String): Result<Any> {
        return try {
            colRef.add(city).await()

            //city successful added
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }

}