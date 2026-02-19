package com.example.bikerentandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bikerentandroid.api.ApiClient
import com.example.bikerentandroid.model.Bike
import com.example.bikerentandroid.model.Ride
import com.example.bikerentandroid.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Displays bike details. Type, Price, Status. Shows "Start Rent" button only when status is Available.
 */
class BikeInfoFragment : Fragment() {

    private var bikeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bikeId = it.getString(ARG_BIKE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_bike_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = bikeId
        if (id.isNullOrBlank()) {
            Toast.makeText(requireContext(), "No bike specified", Toast.LENGTH_SHORT).show()
            return
        }
        val bikeIdLong = id.toLongOrNull()
        if (bikeIdLong == null) {
            Toast.makeText(requireContext(), "Invalid bike ID", Toast.LENGTH_SHORT).show()
            return
        }
        loadBike(bikeIdLong)
    }

    private fun loadBike(id: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            val bike = withContext(Dispatchers.IO) {
                val byId = ApiClient.bikeApi.getBikeById(id)
                if (byId.isSuccessful) {
                    byId.body()
                } else null
            }
            if (bike != null) {
                bindBike(bike)
            } else {
                Toast.makeText(requireContext(), "Bike not found", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun bindBike(bike: Bike) {
        view?.findViewById<TextView>(R.id.bikeTypeValue)?.text = bike.type?.takeIf { it.isNotBlank() } ?: "—"
        view?.findViewById<TextView>(R.id.bikePriceValue)?.text =
            bike.price?.let { "%.0f RSD/min".format(it) } ?: "—"

        val statusText = statusDisplay(bike.status)
        val statusValue = view?.findViewById<TextView>(R.id.bikeStatusValue) ?: return
        statusValue.text = statusText
        statusValue.setTextColor(
            when (bike.status) {
                1 -> ContextCompat.getColor(requireContext(), R.color.status_available_green)
                else -> ContextCompat.getColor(requireContext(), R.color.status_unavailable_red)
            }
        )

        val startRentButton = view?.findViewById<Button>(R.id.buttonStartRent) ?: return
        startRentButton.visibility = if (bike.status == 1) View.VISIBLE else View.GONE
        if (bike.status == 1) {
            startRentButton.setOnClickListener {
                startRide(bike)
            }
        }
    }

    private fun startRide(bike: Bike) {
        val userId = SessionManager.getUserId(requireContext())
        if (userId < 0) {
            Toast.makeText(requireContext(), "You must be logged in to start a ride", Toast.LENGTH_SHORT).show()
            return
        }
        val startTime = java.time.LocalDateTime.now().toString()
        val userRef = User(id = userId, username = null, firstName = null, lastName = null, phone = null, email = null)
        val ride = Ride(user = userRef, bike = bike, startTime = startTime)
        viewLifecycleOwner.lifecycleScope.launch {
            val noCurrentRide = withContext(Dispatchers.IO) {
                val response = ApiClient.rideApi.getActiveRide(userId)
                !response.isSuccessful
            }

            if (!noCurrentRide) {
                Toast.makeText(requireContext(), "You already have an active ride!", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val response = withContext(Dispatchers.IO) {
                ApiClient.rideApi.startRide(ride)
            }
            if (response.isSuccessful) {
                val startTimeMillis = System.currentTimeMillis()
                val pricePerMinute = (bike.price ?: 0.0).toFloat()
                findNavController().navigate(
                    R.id.action_bikeInfoFragment_to_rideFragment,
                    bundleOf(
                        "startTimeMillis" to startTimeMillis,
                        "pricePerMinute" to pricePerMinute
                    )
                )
            } else {
                Toast.makeText(requireContext(), "Could not start ride", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun statusDisplay(status: Int?): String = when (status) {
        1 -> "Available"
        0 -> "Unavailable"
        -1 -> "Unavailable"
        else -> "—"
    }

    companion object {
        private const val ARG_BIKE_ID = "bikeId"
    }
}
