package com.example.bikerentandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bikerentandroid.api.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToLong
import java.time.ZoneId

class RideFragment : Fragment() {

    private var updateJob: Job? = null
    private var currentStartTimeMillis: Long = -1L
    private var currentPricePerMinute: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_ride, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noActiveRidesContainer = view.findViewById<View>(R.id.noActiveRidesContainer)
        val rideDetailsContainer = view.findViewById<View>(R.id.rideDetailsContainer)
        val elapsedTimeValue = view.findViewById<TextView>(R.id.elapsedTimeValue)
        val currentPriceValue = view.findViewById<TextView>(R.id.currentPriceValue)
        val endRideButton = view.findViewById<Button>(R.id.endRideButton)

        val startTimeMillis = arguments?.getLong(ARG_START_TIME_MILLIS, -1L) ?: -1L
        val pricePerMinute = arguments?.getFloat(ARG_PRICE_PER_MINUTE, 0f) ?: 0f

        // Just navigated from BikeInfoFragment with new ride args – show details immediately
        if (startTimeMillis > 0 && pricePerMinute >= 0 && rideDetailsContainer != null && elapsedTimeValue != null && currentPriceValue != null) {
            currentStartTimeMillis = startTimeMillis
            currentPricePerMinute = pricePerMinute
            noActiveRidesContainer?.visibility = View.GONE
            rideDetailsContainer.visibility = View.VISIBLE
            startElapsedUpdater(startTimeMillis, pricePerMinute, elapsedTimeValue, currentPriceValue)
            endRideButton?.setOnClickListener { navigateToCameraToEndRide() }
            // Clear arguments so that if we return to this fragment later, we fetch fresh state
            arguments?.remove(ARG_START_TIME_MILLIS)
            arguments?.remove(ARG_PRICE_PER_MINUTE)
            return
        }

        // Opened from nav bar – fetch active ride for current user
        noActiveRidesContainer?.visibility = View.GONE
        rideDetailsContainer?.visibility = View.GONE

        val userId = SessionManager.getUserId(requireContext())
        if (userId < 0) {
            noActiveRidesContainer?.visibility = View.VISIBLE
            endRideButton?.setOnClickListener { }
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val activeRide = withContext(Dispatchers.IO) {
                val response = ApiClient.rideApi.getActiveRide(userId)
                if (response.isSuccessful) response.body() else null
            }
            if (activeRide != null) {
                val startMs = parseStartTimeToMillis(activeRide.startTime)
                val pricePerMinute = (activeRide.bike?.price ?: 0.0).toFloat()
                if (startMs != null && startMs > 0 && rideDetailsContainer != null && elapsedTimeValue != null && currentPriceValue != null) {
                    currentStartTimeMillis = startMs
                    currentPricePerMinute = pricePerMinute
                    noActiveRidesContainer?.visibility = View.GONE
                    rideDetailsContainer.visibility = View.VISIBLE
                    startElapsedUpdater(startMs, pricePerMinute, elapsedTimeValue, currentPriceValue)
                } else {
                    noActiveRidesContainer?.visibility = View.VISIBLE
                }
            } else {
                noActiveRidesContainer?.visibility = View.VISIBLE
            }
            endRideButton?.setOnClickListener { navigateToCameraToEndRide() }
        }
    }

    private fun navigateToCameraToEndRide() {
        val userId = SessionManager.getUserId(requireContext())
        if (userId < 0) {
            Toast.makeText(requireContext(), "Not logged in", Toast.LENGTH_SHORT).show()
            return
        }
        viewLifecycleOwner.lifecycleScope.launch {
            val ride = withContext(Dispatchers.IO) {
                val response = ApiClient.rideApi.getActiveRide(userId)
                if (response.isSuccessful) response.body() else null
            }
            val rideId = ride?.id
            if (rideId != null && currentStartTimeMillis > 0) {
                findNavController().navigate(
                    R.id.cameraFragment,
                    bundleOf(
                        "rideId" to rideId,
                        "startTimeMillis" to currentStartTimeMillis,
                        "pricePerMinute" to currentPricePerMinute
                    )
                )
            } else {
                Toast.makeText(requireContext(), "No active ride", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startElapsedUpdater(
        startTimeMillis: Long,
        pricePerMinute: Float,
        elapsedTimeValue: TextView,
        currentPriceValue: TextView
    ) {
        fun updateElapsedAndPrice() {
            val elapsedMs = System.currentTimeMillis() - startTimeMillis
            elapsedTimeValue.text = formatElapsed(elapsedMs)
            val price = (elapsedMs / 60_000.0) * pricePerMinute
            currentPriceValue.text = "%,d RSD".format(price.roundToLong())
        }
        updateElapsedAndPrice()
        updateJob = viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                delay(1000L)
                if (!isActive) break
                updateElapsedAndPrice()
            }
        }
    }

    private fun parseStartTimeToMillis(startTime: String?): Long? {
        if (startTime.isNullOrBlank()) return null
        return try {
            val raw = startTime.take(19).replace(" ", "T")
            java.time.LocalDateTime.parse(raw)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        } catch (_: Exception) {
            null
        }
    }

    override fun onDestroyView() {
        updateJob?.cancel()
        updateJob = null
        super.onDestroyView()
    }

    private fun formatElapsed(ms: Long): String {
        if (ms < 0) return "0h 0m 0s"
        val totalSeconds = (ms / 1000).toInt()
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return "%dh %dm %ds".format(hours, minutes, seconds)
    }

    companion object {
        private const val ARG_START_TIME_MILLIS = "startTimeMillis"
        private const val ARG_PRICE_PER_MINUTE = "pricePerMinute"
    }
}
