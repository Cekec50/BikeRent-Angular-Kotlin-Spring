package com.example.bikerentandroid.api

import com.example.bikerentandroid.model.Ride
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RideApiService {

    /**
     * Starts a ride. Sends Ride (startTime, bikeId, userId) to backend. Backend marks the bike as rented (status 0).
     */
    @POST("rides/start")
    suspend fun startRide(@Body ride: Ride): Response<Unit>
}
