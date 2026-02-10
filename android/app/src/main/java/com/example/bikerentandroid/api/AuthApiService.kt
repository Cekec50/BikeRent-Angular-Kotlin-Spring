package com.example.bikerentandroid.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/** API service for login and register. Adjust paths and DTOs to match your backend. */
interface AuthApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>
}

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val phone: String,
    val password: String,
    val isAdmin: Boolean
)

data class LoginRequest(
    val username: String,
    val password: String,
    val isAdmin: Boolean
)

data class LoginResponse(
    val token: String? = null,
    val userId: String? = null,
    val message: String? = null
)
