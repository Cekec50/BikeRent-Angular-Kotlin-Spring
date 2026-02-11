package com.example.bikerentandroid

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.camera.view.PreviewView
import kotlin.math.roundToLong
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

    private lateinit var previewView: PreviewView
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService

    private val CAMERA_PERMISSION_REQUEST = 2001

    private val rideId: Long by lazy {
        arguments?.getLong(ARG_RIDE_ID, -1L) ?: -1L
    }
    private val startTimeMillis: Long by lazy {
        arguments?.getLong(ARG_START_TIME_MILLIS, -1L) ?: -1L
    }
    private val pricePerMinute: Float by lazy {
        arguments?.getFloat(ARG_PRICE_PER_MINUTE, 0f) ?: 0f
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_camera, container, false)

        previewView = view.findViewById(R.id.previewView)
        val captureButton = view.findViewById<ImageButton>(R.id.btnCapture)

        cameraExecutor = Executors.newSingleThreadExecutor()

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST
            )
        }

        captureButton.setOnClickListener {
            takePhoto()
        }

        return view
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Log.e("CameraFragment", "Camera binding failed", e)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        if (rideId < 0) {
            Toast.makeText(requireContext(), "No ride to end", Toast.LENGTH_SHORT).show()
            return
        }
        val photoFile = File(
            requireContext().cacheDir,
            "bike_${System.currentTimeMillis()}.jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    navigateToRideFinish(photoFile)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraFragment", "Photo capture failed", exception)
                    Toast.makeText(requireContext(), "Photo capture failed", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun navigateToRideFinish(photoFile: File) {
        if (startTimeMillis < 0) {
            Toast.makeText(requireContext(), "Invalid ride data", Toast.LENGTH_SHORT).show()
            return
        }
        val elapsedMs = System.currentTimeMillis() - startTimeMillis
        val totalPrice = (elapsedMs / 60_000.0 * pricePerMinute).roundToLong()
        val endTimeMillis = System.currentTimeMillis()
        findNavController().navigate(
            R.id.action_cameraFragment_to_rideFinishFragment,
            bundleOf(
                ARG_RIDE_ID to rideId,
                ARG_PHOTO_PATH to photoFile.absolutePath,
                ARG_TOTAL_PRICE_AT_PHOTO to totalPrice,
                ARG_ELAPSED_MS_AT_PHOTO to elapsedMs,
                ARG_END_TIME_MILLIS to endTimeMillis
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val ARG_RIDE_ID = "rideId"
        private const val ARG_START_TIME_MILLIS = "startTimeMillis"
        private const val ARG_PRICE_PER_MINUTE = "pricePerMinute"
        const val ARG_PHOTO_PATH = "photoPath"
        const val ARG_TOTAL_PRICE_AT_PHOTO = "totalPriceAtPhoto"
        const val ARG_ELAPSED_MS_AT_PHOTO = "elapsedMsAtPhoto"
        const val ARG_END_TIME_MILLIS = "endTimeMillis"
    }
}
