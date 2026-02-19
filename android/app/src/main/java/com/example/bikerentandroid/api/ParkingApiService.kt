package com.example.bikerentandroid.api

import com.example.bikerentandroid.model.Parking
import retrofit2.Response
import retrofit2.http.GET

interface ParkingApiService {

    @GET("parkings")
    suspend fun getAllParkings(): Response<List<Parking>>
}
