package com.example.bikerentandroid

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bikerentandroid.api.ApiClient
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

class ScanFragment : Fragment() {

    private lateinit var previewView: androidx.camera.view.PreviewView
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    /** Prevents multiple overlapping validations/navigations when the same QR is detected every frame. */
    private var isProcessingScan = false
    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Camera permission is required to scan QR codes",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_scan, container, false)
        previewView = view.findViewById(R.id.previewView)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        return view
    }


    @OptIn(ExperimentalGetImage::class)
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()

            val scanner = BarcodeScanning.getClient(options)

            val analysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    val image = InputImage.fromMediaImage(
                        mediaImage,
                        imageProxy.imageInfo.rotationDegrees
                    )

                    scanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            for (barcode in barcodes) {
                                val value = barcode.rawValue?.trim() ?: continue
                                val bikeIdLong = value.toLongOrNull()
                                if (bikeIdLong == null) {
                                    activity?.runOnUiThread {
                                        if (isAdded) {
                                            Toast.makeText(
                                                requireContext(),
                                                "Invalid bike ID",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    break
                                }
                                activity?.runOnUiThread {
                                    if (!isAdded) return@runOnUiThread
                                    if (isProcessingScan) return@runOnUiThread
                                    isProcessingScan = true
                                    validateAndNavigateToBikeInfo(value)
                                }
                                break
                            }
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                }
            }

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                viewLifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                analysis
            )

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun validateAndNavigateToBikeInfo(bikeId: String) {
        val id = bikeId.toLongOrNull() ?: return
        viewLifecycleOwner.lifecycleScope.launch {
            val valid = withContext(Dispatchers.IO) {
                val byId = ApiClient.bikeApi.getBikeById(id)
                if (byId.isSuccessful) true
                else {
                    val all = ApiClient.bikeApi.getAllBikes()
                    all.body()?.any { it.id == id } ?: false
                }
            }
            if (!isAdded) return@launch
            if (valid) {
                findNavController().navigate(
                    R.id.action_scanFragment_to_bikeInfoFragment,
                    bundleOf("bikeId" to bikeId)
                )
            } else {
                isProcessingScan = false
                Toast.makeText(requireContext(), "Bike not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
