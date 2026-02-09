package com.example.bikerentandroid.ui

import com.example.bikerentandroid.model.Parking

import android.widget.TextView
import com.example.bikerentandroid.R
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

class ParkingInfoWindow(
    mapView: MapView
) : InfoWindow(R.layout.view_parking_info, mapView) {

    override fun onOpen(item: Any?) {
        val marker = item as Marker
        val parking = marker.relatedObject as Parking

        mView.findViewById<TextView>(R.id.title).text =
            parking.name

    }

    override fun onClose() {}
}
