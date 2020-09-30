package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.joaovitor.tucaprodutosdelimpeza.data.model.Street
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class StreetRepository {

    private var colRef = FirebaseFirestore.getInstance().collection(Firestore.COL_STREETS)

    private var colSalesRef = FirebaseFirestore.getInstance().collection(Firestore.COL_SALES)

    suspend fun getStreets(): List<Street> {
        val querySnapshot = colRef.orderBy(Firestore.STREET_NAME).get().await()

        val streets: MutableList<Street> = mutableListOf()

        for (doc in querySnapshot){
            val street = doc.toObject(Street::class.java)
            street.id = doc.id
            streets.add(street)
        }
        return streets
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

    suspend fun editStreet(street: Street): Result<Any> {
        return try {
            colRef.document(street.id).set(street).await()

            //street successful edited
            updateSaleStreets(street.name)
            Result.Success(null)
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


    private fun updateSaleStreets(streetName: String) {
        GlobalScope.launch {
            val clients = ClientRepository().getClientsByStreet(streetName)

            for (client in clients) {
                val querySnapshotSales = colSalesRef
                    .whereEqualTo(Firestore.SALE_CLIENT_ID, client.id).get().await()

                for (doc in querySnapshotSales) {
                    doc.reference.update(Firestore.SALE_CLIENT_STREET, streetName)
                }
            }
        }
    }
}