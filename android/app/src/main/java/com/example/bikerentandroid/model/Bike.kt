package com.example.bikerentandroid.model

import org.osmdroid.util.GeoPoint

data class Bike(
    val id: Int,
    val location: GeoPoint,
    val type: String,
    val pricePerHour: Int,
    val nearestParking: String
)
