package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.joaovitor.tucaprodutosdelimpeza.data.model.AddressType
import com.joaovitor.tucaprodutosdelimpeza.data.model.Street
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class StreetRepository {

    private var colRef = FirebaseFirestore.getInstance().collection(Firestore.COL_STREETS)

    private var colSalesRef = FirebaseFirestore.getInstance().collection(Firestore.COL_SALES)

    suspend fun getStreets(): Result<List<Street>> {
        return try {
            val querySnapshot = colRef.orderBy(Firestore.STREET_NAME).get().await()

            val streets: MutableList<Street> = mutableListOf()

            for (doc in querySnapshot){
                val street = doc.toObject(Street::class.java)
                street.id = doc.id
                street.type = AddressType.STREET
                streets.add(street)
            }

            Result.Success(streets)
        } catch(e: Exception){
            Result.Error(e)
        }
    }

    suspend fun addStreet(street: Street): Result<Any> {
        return try {
            colRef.add(street).await()

            //street successful added
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }

    suspend fun editStreet(street: Street, newName: String): Result<Any> {
        return try {
            colRef.document(street.id).update(Firestore.STREET_NAME, newName).await()

            //street successful edited
            return updateSaleStreets(street.name,newName)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }

    suspend fun deleteStreet(streetId: String): Result<Any> {
        return try {
            colRef.document(streetId).delete().await()

            //street successful deleted
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }

    private suspend fun updateSaleStreets(streetName: String, newName: String): Result<Any> {
        return try {
            val resultUpdate = ClientRepository().updateClientsByStreet(streetName, newName)

            if(resultUpdate is Result.Success) {
                for (client in resultUpdate.data!!) {
                    val querySnapshotSales = colSalesRef
                        .whereEqualTo(Firestore.SALE_CLIENT_ID, client.id).get().await()

                    for (doc in querySnapshotSales) {
                        doc.reference.update(Firestore.SALE_CLIENT_STREET, newName)
                    }
                }

                Result.Success(null)
            } else {
                resultUpdate as Result.Error
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}