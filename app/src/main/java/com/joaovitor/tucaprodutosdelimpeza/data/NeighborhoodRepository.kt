package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.joaovitor.tucaprodutosdelimpeza.data.model.Neighborhood
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import kotlinx.coroutines.tasks.await

class NeighborhoodRepository {

    private var colRef: CollectionReference =
        FirebaseFirestore.getInstance().collection(Firestore.COL_NEIGHBORHOODS)

    suspend fun getNeighborhoods(): List<Neighborhood> {
        val querySnapshot = colRef.orderBy(Firestore.NEIGHBORHOOD_NAME).get().await()

        val neighborhoods: MutableList<Neighborhood> = mutableListOf()

        for (doc in querySnapshot){
            val neighborhood = doc.toObject(Neighborhood::class.java)
            neighborhood.id = doc.id
            neighborhoods.add(neighborhood)
        }
        return neighborhoods
    }

    suspend fun addNeighborhood(neighborhood: Neighborhood): Result<Any> {
        return try {
            colRef.add(neighborhood).await()

            //neighborhood successful added
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }


    suspend fun editNeighborhood(neighborhood: Neighborhood): Result<Any> {
        return try {
            colRef.document(neighborhood.id).set(neighborhood).await()

            //neighborhood successful edited
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }

    suspend fun deleteNeighborhood(neighborhoodId: String): Result<Any> {
        return try {
            colRef.document(neighborhoodId).delete().await()

            //street successful deleted
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }
}