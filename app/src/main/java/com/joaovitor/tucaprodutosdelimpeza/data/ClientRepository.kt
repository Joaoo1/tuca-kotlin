package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.*
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
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

    suspend fun getClientsByStreet(streetName: String): List<Client> {
        val querySnapshot = colRef
            .orderBy("nome", Query.Direction.ASCENDING)
            .whereEqualTo(Firestore.CLIENT_STREET, streetName)
            .get()
            .await()

        return querySnapshot.map {
            val client = it.toObject(Client::class.java)
            client.id = it.id
            client
        }
    }

    suspend fun getClientsByNeighborhood(neighborhoodName: String): List<Client> {
        val querySnapshot = colRef
            .orderBy("nome", Query.Direction.ASCENDING)
            .whereEqualTo(Firestore.CLIENT_NEIGHBORHOOD, neighborhoodName)
            .get()
            .await()

        return querySnapshot.map {
            val client = it.toObject(Client::class.java)
            client.id = it.id
            client
        }
    }

    suspend fun getClientsByCity(cityName: String): List<Client> {
        val querySnapshot = colRef
            .orderBy("nome", Query.Direction.ASCENDING)
            .whereEqualTo(Firestore.CLIENT_CITY, cityName)
            .get()
            .await()

        return querySnapshot.map {
            val client = it.toObject(Client::class.java)
            client.id = it.id
            client
        }
    }



}
