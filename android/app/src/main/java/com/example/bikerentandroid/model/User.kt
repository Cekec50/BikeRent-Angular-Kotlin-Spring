package com.example.bikerentandroid.model

import com.google.gson.annotations.SerializedName

/** Matches backend User (login response and update response). Password is null in responses. */
data class User(
    val id: Long,
    val username: String?,
    val firstName: String?,
    val lastName: String?,
    val phone: String?,
    val email: String?,
    @SerializedName("isAdmin") val isAdmin: Boolean = false
)
