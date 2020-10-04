package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.joaovitor.tucaprodutosdelimpeza.data.model.GeneralInfo
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import kotlinx.coroutines.tasks.await
import java.util.*

class DashboardRepository {

    private var colRef: CollectionReference =
        FirebaseFirestore.getInstance().collection("dashboard")

    suspend fun getGeneralInfo(): Result<GeneralInfo> {
        return try {
            val querySnapshot = colRef
                .document("generalInfo")
                .get()
                .await()
            val info = querySnapshot.toObject(GeneralInfo::class.java)

            Result.Success(info)
        }catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun updateGeneralInfo():Result<GeneralInfo> {
        val db =  FirebaseFirestore.getInstance()

        return try {
            val generalInfo = GeneralInfo()

            val querySnapshotProducts = db.collection(Firestore.COL_PRODUCTS).get().await()
            generalInfo.products = querySnapshotProducts.documents.size

            val querySnapshotClients = db.collection(Firestore.COL_CLIENTS).get().await()
            generalInfo.clients = querySnapshotClients.documents.size

            val querySnapshotSales = db.collection(Firestore.COL_SALES).get().await()
            generalInfo.sales = querySnapshotSales.documents.size

            generalInfo.updatedAt = Date()

            colRef.document("generalInfo").set(generalInfo)

            Result.Success(generalInfo)
        }catch (e: Exception) {
            Result.Error(e)
        }
    }
}