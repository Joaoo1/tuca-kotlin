package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.joaovitor.tucaprodutosdelimpeza.data.model.City
import com.joaovitor.tucaprodutosdelimpeza.data.model.Street
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CityRepository {

    private var colRef = FirebaseFirestore.getInstance().collection(Firestore.COL_CITIES)

    private var colSalesRef = FirebaseFirestore.getInstance().collection(Firestore.COL_SALES)

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

    suspend fun addCity(city: City): Result<Any> {
        return try {
            colRef.add(city).await()

            //city successful added
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }

    suspend fun editCity(city: City, newName: String): Result<Any> {
        return try {
            colRef.document(city.id).update(Firestore.CITY_NAME, newName).await()

            //city successful edited
            updateSaleCities(city.name, newName)
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }

    suspend fun deleteCity(cityId: String): Result<Any> {
        return try {
            colRef.document(cityId).delete().await()

            //street successful deleted
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }

    private fun updateSaleCities(cityName: String, newName: String) {
        GlobalScope.launch {
            val clients = ClientRepository().updateClientsByCity(cityName, newName)

            for (client in clients) {
                val querySnapshotSales = colSalesRef
                    .whereEqualTo(Firestore.SALE_CLIENT_ID, client.id).get().await()

                for (doc in querySnapshotSales) {
                    doc.reference.update(Firestore.SALE_CLIENT_CITY, newName)
                }
            }
        }
    }

}