package com.example.bikerentandroid.api

import com.example.bikerentandroid.model.History
import com.example.bikerentandroid.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiService {

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") userId: Long, @Body body: UserUpdateDto): Response<User>

    @GET("history/{id}")
    suspend fun getHistory(@Path("id") userId: Long): Response<List<History>>
}

/** Matches backend UserUpdateDto. Send only fields to update; null = don't change. */
data class UserUpdateDto(
    val username: String? = null,
    val password: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val phone: String? = null,
    val email: String? = null
)
