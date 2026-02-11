package com.example.bikerentandroid.api

import com.example.bikerentandroid.model.Ride
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RideApiService {

    /**
     * Starts a ride. Sends Ride (startTime, bikeId, userId) to backend. Backend marks the bike as rented (status 0).
     */
    @POST("rides/start")
    suspend fun startRide(@Body ride: Ride): Response<Unit>

    /**
     * Gets the currently active ride for the user. Returns 404 or empty when there is no active ride.
     */
    @GET("rides/active")
    suspend fun getActiveRide(@Query("userId") userId: Long): Response<Ride>
}
