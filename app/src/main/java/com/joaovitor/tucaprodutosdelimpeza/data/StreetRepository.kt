package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.Street
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import kotlinx.coroutines.tasks.await


class StreetRepository {

    private var colRef: CollectionReference =
        FirebaseFirestore.getInstance().collection(Firestore.COL_STREETS)

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

    suspend fun addStreet(street: String): Result<Any> {
        return try {
            colRef.add(street).await()

            //street successful added
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }
}