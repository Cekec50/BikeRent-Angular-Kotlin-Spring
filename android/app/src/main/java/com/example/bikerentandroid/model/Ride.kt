package com.example.bikerentandroid.model

/**
 * Matches backend Ride entity: id, user, bike, startTime.
 * startTime: ISO-8601 date-time string (e.g. "2026-02-10T14:30:00").
 * When sending (POST start): set user (with id), bike (with id), startTime.
 */
data class Ride(
    val id: Long? = null,
    val user: User? = null,
    val bike: Bike? = null,
    val startTime: String
)
