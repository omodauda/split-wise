package com.example.splitwise.model

data class SignupFormState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val fullName: String = "",
    val fullNameError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null
)
