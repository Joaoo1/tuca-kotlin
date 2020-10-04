package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception

/**
 * Class that handles requesting and assigning available sales IDs
 */

class SaleIdRepository {

    private var colRef: CollectionReference =
        FirebaseFirestore.getInstance().collection(Firestore.COL_SALES_ID)

    /** Get an available ID for sale */
    suspend fun generateSaleId(): Result<Int> {
        return try {
            val result  = colRef.get().await()

            // Get all used IDs
            val ids: List<Int> = result.map { (it.get("venda") as Long).toInt() }

            //Return an available ID
            for(i in 1000..99999) {
                if(!ids.contains(i)) return Result.Success(i)
            }

            //No available ID found
            throw FirebaseFirestoreException("error finding an available sale id ",
                FirebaseFirestoreException.Code.UNKNOWN)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /** A ID was used to a sale, thus set it as unavailable */
    fun setIdAsUsed(saleId: Int) {
        val data: MutableMap<String, Int> = HashMap()
        data["venda"] = saleId
        FirebaseFirestore.getInstance().collection(Firestore.COL_SALES_ID).add(data)
            .addOnFailureListener {
            FirebaseCrashlytics.getInstance().recordException(it)
        }
    }
}