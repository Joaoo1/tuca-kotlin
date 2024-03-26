package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.firestore.FirebaseFirestore
import com.joaovitor.tucaprodutosdelimpeza.data.model.User
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class UserRepository {
    private var colRef = FirebaseFirestore.getInstance().collection(Firestore.COL_USERS)

    suspend fun getSellers(): Result<List<User>> {
        return try {
            val querySnapshot = colRef.orderBy(Firestore.USER_NAME).get().await()
            val users = querySnapshot.toObjects(User::class.java)
            Result.Success(users)
        } catch(e: Exception){
            Result.Error(e)
        }
    }
}