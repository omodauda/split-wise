package com.example.splitwise.data.network.model

data class LoginRequest(
    val email: String,
    val password: String
)
data class LoginResponse(
    val message: String,
    val data: AuthData
)
data class AuthData(
   val token: String,
   val user: AuthUserData
)
data class AuthUserData(
    val id: String,
    val email: String,
    val fullName: String,
    val avatar: String?
)