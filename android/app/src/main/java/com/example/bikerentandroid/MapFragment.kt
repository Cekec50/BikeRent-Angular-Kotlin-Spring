package com.example.bikerentandroid

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.bikerentandroid.api.ApiClient
import com.example.bikerentandroid.model.Bike
import com.example.bikerentandroid.model.Parking
import com.example.bikerentandroid.ui.BikeInfoWindow
import com.example.bikerentandroid.ui.ParkingInfoWindow
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay





class MapFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var locationOverlay: MyLocationNewOverlay

    private val LOCATION_PERMISSION_REQUEST = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(
            requireContext(),
            requireContext().getSharedPreferences("osmdroid", 0)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = view.findViewById(R.id.mapView)

        mapView.setTileSource(
            XYTileSource(
                "CartoPositron",
                1, 19, 256, ".png",
                arrayOf(
                    "https://a.basemaps.cartocdn.com/light_all/",
                    "https://b.basemaps.cartocdn.com/light_all/",
                    "https://c.basemaps.cartocdn.com/light_all/"
                )
            )
        )

        mapView.setMultiTouchControls(true)
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        mapView.controller.setZoom(18.0)

        locationOverlay =
            MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), mapView)
        locationOverlay.enableMyLocation()
        locationOverlay.enableFollowLocation()
        mapView.overlays.add(locationOverlay)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
        }

        loadBikesFromBackend()
        addParkingMarkers()

        return view
    }

    private fun loadBikesFromBackend() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = ApiClient.bikeApi.getAllBikes()
                if (response.isSuccessful) {
                    val bikes = response.body().orEmpty()
                    addBikeMarkers(bikes)
                } else {
                    Log.e("MapFragment", "Failed to load bikes: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MapFragment", "Error loading bikes", e)
            }
        }
    }

    private fun addBikeMarkers(bikes: List<Bike>) {
        for (bike in bikes) {
            val geoPoint = bike.toGeoPoint() ?: continue
            val marker = Marker(mapView)
            marker.position = geoPoint
            marker.icon = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_bike_pin
            )

            marker.relatedObject = bike
            marker.infoWindow = BikeInfoWindow(mapView)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            marker.setOnMarkerClickListener { m, _ ->
                if (m.isInfoWindowShown) {
                    m.closeInfoWindow()
                } else {
                    m.showInfoWindow()
                }
                true
            }
            mapView.overlays.add(marker)
        }
        mapView.invalidate()
    }

    private fun addParkingMarkers() {
        for (spot in getParkings()) {
            val marker = Marker(mapView)
            marker.position = spot.location
            marker.icon = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_parking_pin
            )
            marker.relatedObject = spot
            marker.infoWindow = ParkingInfoWindow(mapView)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            marker.setOnMarkerClickListener { m, _ ->
                if (m.isInfoWindowShown) {
                    m.closeInfoWindow()
                } else {
                    m.showInfoWindow()
                }
                true
            }
            mapView.overlays.add(marker)
        }
    }

    private fun getParkings(): List<Parking> = listOf(
        Parking(1, GeoPoint(44.805196, 20.479203), "Vukov Spomenik Parking")
    )

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        locationOverlay.enableMyLocation()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        locationOverlay.disableMyLocation()
    }
}
