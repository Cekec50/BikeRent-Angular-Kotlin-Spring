package com.example.bikerentandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

class RideFragment : Fragment() {

    private var updateJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_ride, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startTimeMillis = arguments?.getLong(ARG_START_TIME_MILLIS, -1L) ?: -1L
        val pricePerHour = arguments?.getFloat(ARG_PRICE_PER_HOUR, 0f) ?: 0f

        val noActiveRidesContainer = view.findViewById<View>(R.id.noActiveRidesContainer)
        val rideDetailsContainer = view.findViewById<View>(R.id.rideDetailsContainer)
        val elapsedTimeValue = view.findViewById<TextView>(R.id.elapsedTimeValue)
        val currentPriceValue = view.findViewById<TextView>(R.id.currentPriceValue)
        val endRideButton = view.findViewById<Button>(R.id.endRideButton)

        noActiveRidesContainer?.visibility = View.VISIBLE
        rideDetailsContainer?.visibility = View.GONE

        if (startTimeMillis > 0 && rideDetailsContainer != null && elapsedTimeValue != null && currentPriceValue != null) {
            noActiveRidesContainer?.visibility = View.GONE
            rideDetailsContainer.visibility = View.VISIBLE

            fun updateElapsedAndPrice() {
                val elapsedMs = System.currentTimeMillis() - startTimeMillis
                elapsedTimeValue.text = formatElapsed(elapsedMs)
                val price = (elapsedMs / 3_600_000.0) * pricePerHour
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

        endRideButton?.setOnClickListener {
            findNavController().navigate(R.id.cameraFragment)
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
        private const val ARG_PRICE_PER_HOUR = "pricePerHour"
    }
}
