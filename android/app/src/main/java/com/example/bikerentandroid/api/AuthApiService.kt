package com.example.bikerentandroid.api

import com.example.bikerentandroid.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/** API service for login and register. Backend returns User on success. */
interface AuthApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<User>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<User>
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

