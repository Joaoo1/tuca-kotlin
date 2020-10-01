package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.joaovitor.tucaprodutosdelimpeza.data.model.LoggedInUser
import kotlinx.coroutines.tasks.await

/**
 * Class that requests authentication and user information from the firestore and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository {

    // in-memory cache of the loggedInUser object
    private var user: LoggedInUser? = null

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
    }

    suspend fun login(email: String, password: String): Result<LoggedInUser> {
        return try {
            // handle login
            val firebaseUser = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await().user
            val user = LoggedInUser(firebaseUser?.uid, firebaseUser?.email!!)

            setLoggedInUser(user)

            Result.Success(user)
        } catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        }

    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}