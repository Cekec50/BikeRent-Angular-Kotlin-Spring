package com.example.bikerentandroid.api

import com.example.bikerentandroid.model.Bike
import retrofit2.Response
import retrofit2.http.GET

interface BikeApiService {

    @GET("bikes")
    suspend fun getAllBikes(): Response<List<Bike>>
}
