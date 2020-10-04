package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class ClientRepository {

    private var colRef: CollectionReference =
        FirebaseFirestore.getInstance().collection("clientes")

    suspend fun getClients(): Result<List<Client>> {
        return try {
            val querySnapshot = colRef
                .orderBy("nome", Query.Direction.ASCENDING)
                .get()
                .await()

            val clients =  querySnapshot.map {
                val client = it.toObject(Client::class.java)
                client.id = it.id
                client
            }

            Result.Success(clients)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun addClient(client: Client): Result<Void> {
        return try {
            colRef.add(client).await()

            //client successful added
            Result.Success(null)
        }catch (e: Exception) {
            Result.Error(e)
        }
    }

     suspend fun editClient(client: Client): Result<Void> {
        return try {
            colRef.document(client.id).set(client, SetOptions.merge()).await()

            //client successful edit
            val resultUpdateSales = updateClientSales(client)
            if(resultUpdateSales is Result.Success) {
                Result.Success(null)
            } else {
                resultUpdateSales as Result.Error
            }
        }catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun deleteClient(clientId: String): Result<Void> {
        return try {
            colRef.document(clientId).delete().await()

            //client successful added
            Result.Success(null)
        }catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun updateClientsByStreet(streetName: String, newName: String): Result<List<Client>> {
        return try {
            val querySnapshot = colRef
                .whereEqualTo(Firestore.CLIENT_STREET, streetName)
                .get()
                .await()

            val clients = querySnapshot.map {
                it.reference.update(Firestore.CLIENT_STREET, newName)

                val client = it.toObject(Client::class.java)
                client.id = it.id
                client
            }

            Result.Success(clients)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun updateClientsByNeighborhood(neighborhoodName: String, newName: String): Result<List<Client>> {
        return try {
            val querySnapshot = colRef
            .whereEqualTo(Firestore.CLIENT_NEIGHBORHOOD, neighborhoodName)
            .get()
            .await()

            val clients = querySnapshot.map {
                it.reference.update(Firestore.CLIENT_NEIGHBORHOOD, newName)

            val client = it.toObject(Client::class.java)
            client.id = it.id
            client }

            Result.Success(clients)
        }catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun updateClientsByCity(cityName: String, newName: String): Result<List<Client>> {
        return try {
            val querySnapshot = colRef
                .whereEqualTo(Firestore.CLIENT_CITY, cityName)
                .get()
                .await()

            val clients = querySnapshot.map {
                it.reference.update(Firestore.CLIENT_CITY, newName)

                val client = it.toObject(Client::class.java)
                client.id = it.id
                client
            }

            Result.Success(clients)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private suspend fun updateClientSales(client: Client): Result<Any> {
        return try {
            val data = client.toSaleHashMap()
            val querySnapshot = FirebaseFirestore
                .getInstance()
                .collection(Firestore.COL_SALES)
                .whereEqualTo(Firestore.SALE_CLIENT_ID, client.id)
                .get()
                .await()

            for (doc in querySnapshot) {
                doc.reference.update(data)
            }

            Result.Success(null)
        } catch (e: Exception) {
            Result.Error(e)
        }



    }
}
