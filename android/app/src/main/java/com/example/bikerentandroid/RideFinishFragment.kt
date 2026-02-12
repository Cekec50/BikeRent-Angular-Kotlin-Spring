package com.example.bikerentandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.core.os.bundleOf
import com.example.bikerentandroid.api.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.time.ZoneId

class RideFinishFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_ride_finish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rideId = arguments?.getLong(ARG_RIDE_ID, -1L) ?: -1L
        val photoPath = arguments?.getString(ARG_PHOTO_PATH).orEmpty()
        val totalPriceAtPhoto = arguments?.getLong(ARG_TOTAL_PRICE_AT_PHOTO, 0L) ?: 0L
        val elapsedMsAtPhoto = arguments?.getLong(ARG_ELAPSED_MS_AT_PHOTO, 0L) ?: 0L
        val endTimeMillis = arguments?.getLong(ARG_END_TIME_MILLIS, 0L) ?: 0L

        view.findViewById<TextView>(R.id.savedTimeValue).text = formatElapsed(elapsedMsAtPhoto)
        view.findViewById<TextView>(R.id.savedPriceValue).text = "%,d RSD".format(totalPriceAtPhoto)

        view.findViewById<Button>(R.id.finishButton).setOnClickListener {
            if (rideId < 0 || photoPath.isEmpty()) {
                Toast.makeText(requireContext(), "Invalid ride or photo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val photoFile = File(photoPath)
            if (!photoFile.exists()) {
                Toast.makeText(requireContext(), "Photo not found", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            uploadAndFinishRide(rideId, photoFile, endTimeMillis, totalPriceAtPhoto, elapsedMsAtPhoto)
        }

        view.findViewById<Button>(R.id.reportProblemButton).setOnClickListener {
            // Navigate to Camera to take a picture of the problem
            // Pass existing data so ReportFragment can eventually finish the ride
            findNavController().navigate(
                R.id.cameraFragment,
                bundleOf(
                    ARG_RIDE_ID to rideId,
                    "flowMode" to "report",
                    "existingPhotoPath" to photoPath,
                    ARG_TOTAL_PRICE_AT_PHOTO to totalPriceAtPhoto,
                    ARG_ELAPSED_MS_AT_PHOTO to elapsedMsAtPhoto,
                    ARG_END_TIME_MILLIS to endTimeMillis
                )
            )
        }
    }

    private fun uploadAndFinishRide(
        rideId: Long,
        photoFile: File,
        endTimeMillis: Long,
        totalPrice: Long,
        elapsedMs: Long
    ) {
        val endTimeIso = java.time.Instant.ofEpochMilli(endTimeMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
            .toString()
        val durationMinutes = elapsedMs / 60_000
        viewLifecycleOwner.lifecycleScope.launch {
            val ok = withContext(Dispatchers.IO) {
                try {
                    val requestBody = photoFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val photoPart = MultipartBody.Part.createFormData("photo", photoFile.name, requestBody)
                    val rideIdBody = rideId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    val endTimeBody = endTimeIso.toRequestBody("text/plain".toMediaTypeOrNull())
                    val totalPriceBody = totalPrice.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    val durationBody = durationMinutes.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    val endResponse = ApiClient.rideApi.endRideWithPhoto(
                        photoPart,
                        rideIdBody,
                        endTimeBody,
                        totalPriceBody,
                        durationBody
                    )
                    endResponse.isSuccessful
                } catch (e: Exception) {
                    false
                }
            }
            if (!isAdded) return@launch
            if (ok) {
                findNavController().popBackStack(R.id.rideFragment, false)
            } else {
                Toast.makeText(requireContext(), "Failed to finish ride", Toast.LENGTH_SHORT).show()
            }
        }
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
        private const val ARG_RIDE_ID = "rideId"
        private const val ARG_PHOTO_PATH = "photoPath"
        private const val ARG_TOTAL_PRICE_AT_PHOTO = "totalPriceAtPhoto"
        private const val ARG_ELAPSED_MS_AT_PHOTO = "elapsedMsAtPhoto"
        private const val ARG_END_TIME_MILLIS = "endTimeMillis"
    }
}
