package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.*
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ClientRepository {

    private var colRef: CollectionReference =
        FirebaseFirestore.getInstance().collection("clientes")

    suspend fun getClients(): List<Client> {
        val querySnapshot = colRef
            .orderBy("nome", Query.Direction.ASCENDING)
            .get()
            .await()

        return querySnapshot.map {
            val client = it.toObject(Client::class.java)
            client.id = it.id
            client
        }
    }

    suspend fun addClient(client: Client): Result<Void> {
        return try {
            colRef.add(client).await()

            //client successful added
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }

     suspend fun editClient(client: Client): Result<Void> {
        return try {
            colRef.document(client.id).set(client, SetOptions.merge()).await()

            //client successful edit
            updateClientSales(client)
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }

    suspend fun deleteClient(clientId: String): Result<Void> {
        return try {
            colRef.document(clientId).delete().await()

            //client successful added
            Result.Success(null)
        }catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }
    }

    suspend fun updateClientsByStreet(streetName: String, newName: String): List<Client> {
        val querySnapshot = colRef
            .whereEqualTo(Firestore.CLIENT_STREET, streetName)
            .get()
            .await()

        return querySnapshot.map {
            it.reference.update(Firestore.CLIENT_STREET, newName)

            val client = it.toObject(Client::class.java)
            client.id = it.id
            client
        }
    }

    suspend fun updateClientsByNeighborhood(neighborhoodName: String, newName: String): List<Client> {
        val querySnapshot = colRef
            .whereEqualTo(Firestore.CLIENT_NEIGHBORHOOD, neighborhoodName)
            .get()
            .await()

        return querySnapshot.map {
            it.reference.update(Firestore.CLIENT_NEIGHBORHOOD, newName)

            val client = it.toObject(Client::class.java)
            client.id = it.id
            client
        }
    }

    suspend fun updateClientsByCity(cityName: String, newName: String): List<Client> {
        val querySnapshot = colRef
            .whereEqualTo(Firestore.CLIENT_CITY, cityName)
            .get()
            .await()

        return querySnapshot.map {
            it.reference.update(Firestore.CLIENT_CITY, newName)

            val client = it.toObject(Client::class.java)
            client.id = it.id
            client
        }
    }


    private fun updateClientSales(client: Client) {
        GlobalScope.launch {
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
        }

    }
}
