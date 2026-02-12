package com.example.bikerentandroid.api

import com.example.bikerentandroid.model.Ride
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
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

    /**
     * Ends the ride: uploads photo + endTime, totalPrice, duration. Backend creates History and deletes the active ride.
     * endTime: ISO-8601 (e.g. "2026-02-10T14:30:00"). duration: minutes (long).
     */
    @Multipart
    @POST("rides/end")
    suspend fun endRideWithPhoto(
        @Part photo: MultipartBody.Part,
        @Part("rideId") rideId: RequestBody,
        @Part("endTime") endTime: RequestBody,
        @Part("totalPrice") totalPrice: RequestBody,
        @Part("duration") duration: RequestBody
    ): Response<Unit>

    /**
     * Creates a problem report for a ride.
     */
    @Multipart
    @POST("reports")
    suspend fun createReport(
        @Part photo: MultipartBody.Part,
        @Part("rideId") rideId: RequestBody,
        @Part("description") description: RequestBody
    ): Response<Unit>

    /**
     * Deletes a ride record directly. Note: For finishing a ride, use `endRideWithPhoto`.
     */
    @DELETE("rides/{id}")
    suspend fun deleteRide(@Path("id") rideId: Long): Response<Unit>
}
