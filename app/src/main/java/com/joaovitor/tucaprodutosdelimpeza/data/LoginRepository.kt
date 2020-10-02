package com.joaovitor.tucaprodutosdelimpeza.data

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.joaovitor.tucaprodutosdelimpeza.data.model.LoggedInUser
import com.joaovitor.tucaprodutosdelimpeza.data.model.User
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import kotlinx.coroutines.tasks.await

/**
 * Class that requests authentication and user information from the firestore and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(private val context: Context) {

    private val auth = FirebaseAuth.getInstance()
    fun logout() {
        auth.signOut()
    }

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            // handle login
            val firebaseUser = auth.signInWithEmailAndPassword(email, password).await().user
            val user = User(uid = firebaseUser?.uid, email = firebaseUser?.email!!)
            user.displayName = getDisplayName(user.uid!!)

            setLoggedInUser(user)

            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }

    }

    private fun setLoggedInUser(user: User) {
        val sharedPreference = context.getSharedPreferences("login", Context.MODE_PRIVATE)
        sharedPreference.edit().putString("user_email", user.email).apply()
        sharedPreference.edit().putString("user_name", user.displayName).apply()
        sharedPreference.edit().putBoolean("is_logged_in", true).apply()
    }

    private suspend fun getDisplayName(userUid: String): String? {
        return FirebaseFirestore.getInstance().collection("users").document(userUid)
            .get().await().getString(Firestore.USER_NAME)
    }
}