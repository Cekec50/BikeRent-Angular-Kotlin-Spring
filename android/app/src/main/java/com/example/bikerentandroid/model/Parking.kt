package com.example.bikerentandroid.model

import org.osmdroid.util.GeoPoint

data class Parking(
    val id: Int,
    val location: GeoPoint,
    val name: String
)
