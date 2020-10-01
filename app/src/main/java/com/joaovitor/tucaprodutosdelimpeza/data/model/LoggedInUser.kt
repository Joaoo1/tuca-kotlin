package com.joaovitor.tucaprodutosdelimpeza.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userUid: String? = "",
    val displayName: String? = ""
)