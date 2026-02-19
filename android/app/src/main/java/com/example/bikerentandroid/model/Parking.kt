package com.example.bikerentandroid.model

import org.osmdroid.util.GeoPoint

data class Parking(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String
) {
    fun toGeoPoint(): GeoPoint = GeoPoint(latitude, longitude)
}
