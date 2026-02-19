package com.example.bikerentandroid.model

import org.osmdroid.util.GeoPoint

/**
 * Matches backend Bike: id, type, price, status, location, latitude, longitude.
 * Status: 1 = Available, 0 = Rented, -1 = Unavailable.
 */
data class Bike(
    val id: Long,
    val type: String?,
    val price: Double?,
    val status: Int?,
    val location: String?,
    val latitude: Double?,
    val longitude: Double?,
    val nearestParking: Parking?
) {
    /** GeoPoint for map marker; null if latitude or longitude is missing. */
    fun toGeoPoint(): GeoPoint? =
        if (latitude != null && longitude != null) GeoPoint(latitude, longitude) else null
}
