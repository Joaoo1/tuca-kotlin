package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.FirebaseFirestore
import com.joaovitor.tucaprodutosdelimpeza.data.model.AddressType
import com.joaovitor.tucaprodutosdelimpeza.data.model.City
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class CityRepository {

    private var colRef = FirebaseFirestore.getInstance().collection(Firestore.COL_CITIES)

    private var colSalesRef = FirebaseFirestore.getInstance().collection(Firestore.COL_SALES)

    suspend fun getCities(): Result<List<City>> {
        return try {
            val querySnapshot = colRef.orderBy(Firestore.CITY_NAME).get().await()

            val cities: MutableList<City> = mutableListOf()

            for (doc in querySnapshot) {
                val city = doc.toObject(City::class.java)
                city.id = doc.id
                city.type = AddressType.CITY
                cities.add(city)
            }

            //Cities list get with success
            Result.Success(cities)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun addCity(city: City): Result<City> {
        return try {
            colRef.add(city).await()

            //city successful added
            Result.Success(null)
        }catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun editCity(city: City, newName: String): Result<Any> {
        return try {
            colRef.document(city.id).update(Firestore.CITY_NAME, newName).await()

            /*
             * City successful edited
             * Now update the city field on sales
             */
            val resultUpdate = updateSaleCities(city.name, newName)

            if (resultUpdate is Result.Success) {
                Result.Success(null)
            } else {
                resultUpdate as Result.Error
            }
        }catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun deleteCity(cityId: String): Result<Any> {
        return try {
            colRef.document(cityId).delete().await()

            //street successful deleted
            Result.Success(null)
        }catch (e: Exception) {
            Result.Error(e)
        }
    }

    private suspend fun updateSaleCities(cityName: String, newName: String): Result<Any> {
        return try {
            /* Update all Clients records that are registered in the updated city */
            val resultUpdate = ClientRepository().updateClientsByCity(cityName, newName)

            //Check if clients update was successfully
            if(resultUpdate is Result.Error) {
                return resultUpdate
            }

            /* Update city field for all clients sales records */
            for (client in (resultUpdate as Result.Success).data!!) {
                val querySnapshotSales = colSalesRef
                    .whereEqualTo(Firestore.SALE_CLIENT_ID, client.id).get().await()

                for (doc in querySnapshotSales) {
                    doc.reference.update(Firestore.SALE_CLIENT_CITY, newName)
                }
            }

            Result.Success(null)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}