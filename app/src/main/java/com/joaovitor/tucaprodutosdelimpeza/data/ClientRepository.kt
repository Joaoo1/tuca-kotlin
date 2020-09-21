package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
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
}
