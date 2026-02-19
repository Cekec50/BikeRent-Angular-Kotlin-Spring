package com.example.bikerentandroid.ui

import android.widget.TextView
import com.example.bikerentandroid.R
import com.example.bikerentandroid.model.Bike
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

class BikeInfoWindow(
    mapView: MapView
) : InfoWindow(R.layout.view_bike_info, mapView) {

    override fun onOpen(item: Any?) {
        val marker = item as Marker
        val bike = marker.relatedObject as Bike

        mView.findViewById<TextView>(R.id.title).text =
            "Bike #${bike.id}"

        mView.findViewById<TextView>(R.id.type).text =
            "Type: ${bike.type.orEmpty()}"

        mView.findViewById<TextView>(R.id.price).text =
            "Price: ${bike.price?.let { "%.0f".format(it) }.orEmpty()} RSD/min"

        val statusText = when (bike.status) {
            1 -> "Available"
            0 -> "Unavailable"
            -1 -> "Removed"
            else -> "Unknown"
        }
        mView.findViewById<TextView>(R.id.status).text = "Status: $statusText"

        mView.findViewById<TextView>(R.id.currentLocation).text =
            "Location: ${bike.location.orEmpty()}"

        mView.findViewById<TextView>(R.id.nearestParking).text =
            "Nearest parking: ${bike.nearestParking?.name.orEmpty()} (${bike.distanceToNearestParking?.toString().orEmpty()}m)"
    }

    override fun onClose() {}
}
