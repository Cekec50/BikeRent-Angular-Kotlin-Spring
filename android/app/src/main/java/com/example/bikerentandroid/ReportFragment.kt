package com.example.bikerentandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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

class ReportFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Assumes a layout file named fragment_report exists with etDescription and btnSubmit
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rideId = arguments?.getLong("rideId", -1L) ?: -1L
        val finishPhotoPath = arguments?.getString("finishPhotoPath").orEmpty()
        val problemPhotoPath = arguments?.getString("problemPhotoPath").orEmpty()
        val totalPrice = arguments?.getLong("totalPriceAtPhoto", 0L) ?: 0L
        val elapsedMs = arguments?.getLong("elapsedMsAtPhoto", 0L) ?: 0L
        val endTimeMillis = arguments?.getLong("endTimeMillis", 0L) ?: 0L

        val etDescription = view.findViewById<EditText>(R.id.problemDescription)
        val reportButton = view.findViewById<Button>(R.id.reportButton)

        reportButton.setOnClickListener {
            val description = etDescription.text.toString().trim()
            if (description.isEmpty()) {
                Toast.makeText(requireContext(), "Please describe the problem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (rideId < 0 || finishPhotoPath.isEmpty() || problemPhotoPath.isEmpty()) {
                Toast.makeText(requireContext(), "Missing ride data or photos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            uploadReportAndFinish(rideId, finishPhotoPath, problemPhotoPath, description, endTimeMillis, totalPrice, elapsedMs)
        }
    }

    private fun uploadReportAndFinish(
        rideId: Long,
        finishPath: String,
        problemPath: String,
        description: String,
        endTimeMillis: Long,
        totalPrice: Long,
        elapsedMs: Long
    ) {
        val finishFile = File(finishPath)
        val problemFile = File(problemPath)
        val endTimeIso = java.time.Instant.ofEpochMilli(endTimeMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
            .toString()
        val durationMinutes = elapsedMs / 60_000

        viewLifecycleOwner.lifecycleScope.launch {
            val success = withContext(Dispatchers.IO) {
                try {
                    // 1. End the ride first
                    val finishBody = finishFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    // Note: endRideWithPhoto expects the part name "photo"
                    val finishPart = MultipartBody.Part.createFormData("photo", finishFile.name, finishBody)
                    val rideIdBody = rideId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    val endTimeBody = endTimeIso.toRequestBody("text/plain".toMediaTypeOrNull())
                    val priceBody = totalPrice.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    val durationBody = durationMinutes.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                    val problemBody = problemFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val problemPart = MultipartBody.Part.createFormData("photo", problemFile.name, problemBody)
                    val descBody = description.toRequestBody("text/plain".toMediaTypeOrNull())

                    val reportResponse =  ApiClient.rideApi.createReport(problemPart, rideIdBody, descBody)
                    val endResponse = ApiClient.rideApi.endRideWithPhoto(finishPart, rideIdBody, endTimeBody, priceBody, durationBody)
                    reportResponse.isSuccessful && endResponse.isSuccessful
                } catch (e: Exception) {
                    false
                }
            }
            if (success) {
                findNavController().popBackStack(R.id.rideFragment, false)
                Toast.makeText(requireContext(), "Report submitted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed to submit report", Toast.LENGTH_SHORT).show()
            }
        }
    }
}