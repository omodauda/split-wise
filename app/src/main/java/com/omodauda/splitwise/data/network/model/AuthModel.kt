package com.omodauda.splitwise.data.network.model

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

data class SignupRequest(
    val fullName: String,
    val email: String,
    val password: String
)

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)

data class ChangePasswordResponse(
    val message: String
)

data class UpdateProfileRequest(
    val fullName: String
)
data class UpdateProfileResponse(
    val message: String,
    val data: AuthUserData
)

data class DeleteAccountRequest(
    val password: String
)

data class DeleteAccountResponse(
    val message: String
)
