package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.joaovitor.tucaprodutosdelimpeza.data.model.Neighborhood
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class NeighborhoodRepository {

    private var colRef = FirebaseFirestore.getInstance().collection(Firestore.COL_NEIGHBORHOODS)

    private var colSalesRef = FirebaseFirestore.getInstance().collection(Firestore.COL_SALES)

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
            updateSaleNeighborhoods(neighborhood.name)
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


    private fun updateSaleNeighborhoods(neighborhoodName: String) {
        GlobalScope.launch {
            val clients = ClientRepository().getClientsByNeighborhood(neighborhoodName)

            for (client in clients) {
                val querySnapshotSales = colSalesRef
                    .whereEqualTo(Firestore.SALE_CLIENT_ID, client.id).get().await()

                for (doc in querySnapshotSales) {
                    doc.reference.update(Firestore.SALE_CLIENT_NEIGHBORHOOD, neighborhoodName)
                }
            }
        }
    }
}