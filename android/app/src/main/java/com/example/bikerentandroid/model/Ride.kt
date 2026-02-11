package com.example.bikerentandroid.model

/**
 * Sent to backend when starting a ride. Backend marks the bike as rented (status 0).
 * startTime: ISO-8601 date-time string (e.g. "2026-02-10T14:30:00").
 */
data class Ride(
    val startTime: String,
    val bikeId: Long,
    val userId: Long
)
