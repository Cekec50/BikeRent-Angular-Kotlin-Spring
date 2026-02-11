package com.example.bikerentandroid.api

import com.example.bikerentandroid.model.Bike
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BikeApiService {

    @GET("bikes")
    suspend fun getAllBikes(): Response<List<Bike>>

    @GET("bikes/{id}")
    suspend fun getBikeById(@Path("id") id: Long): Response<Bike>
}
