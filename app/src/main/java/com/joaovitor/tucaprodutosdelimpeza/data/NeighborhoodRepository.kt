package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.FirebaseFirestore
import com.joaovitor.tucaprodutosdelimpeza.data.model.Neighborhood
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class NeighborhoodRepository {

    private var colRef = FirebaseFirestore.getInstance().collection(Firestore.COL_NEIGHBORHOODS)

    private var colSalesRef = FirebaseFirestore.getInstance().collection(Firestore.COL_SALES)

    suspend fun getNeighborhoods(): Result<List<Neighborhood>> {
        return try {
            val querySnapshot = colRef.orderBy(Firestore.NEIGHBORHOOD_NAME).get().await()

            val neighborhoods: MutableList<Neighborhood> = mutableListOf()

            for (doc in querySnapshot){
                val neighborhood = doc.toObject(Neighborhood::class.java)
                neighborhood.id = doc.id
                neighborhoods.add(neighborhood)
            }

            Result.Success(neighborhoods)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun addNeighborhood(neighborhood: Neighborhood): Result<Neighborhood> {
        return try {
            colRef.add(neighborhood).await()

            //neighborhood successful added
            Result.Success(null)
        }catch (e: Exception) {
            Result.Error(e)
        }
    }


    suspend fun editNeighborhood(neighborhood: Neighborhood, newName: String): Result<Any> {
        return try {
            colRef.document(neighborhood.id).update(Firestore.NEIGHBORHOOD_NAME, newName).await()

            //neighborhood successful edited
            return updateSaleNeighborhoods(neighborhood.name, newName)
        }catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun deleteNeighborhood(neighborhoodId: String): Result<Any> {
        return try {
            colRef.document(neighborhoodId).delete().await()

            //neighborhood successful deleted
            Result.Success(null)
        }catch (e: Exception) {
            Result.Error(e)
        }
    }

    private suspend fun updateSaleNeighborhoods(neighborhoodName: String, newName: String): Result<Any> {
        return try {
            val resultUpdate = ClientRepository().updateClientsByNeighborhood(neighborhoodName, newName)

            if(resultUpdate is Result.Error) {
                return resultUpdate
            }

            for (client in (resultUpdate as Result.Success).data!!) {
                val querySnapshotSales = colSalesRef
                    .whereEqualTo(Firestore.SALE_CLIENT_ID, client.id).get().await()

                for (doc in querySnapshotSales) {
                    doc.reference.update(Firestore.SALE_CLIENT_NEIGHBORHOOD, newName)
                }
            }

            Result.Success(null)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}