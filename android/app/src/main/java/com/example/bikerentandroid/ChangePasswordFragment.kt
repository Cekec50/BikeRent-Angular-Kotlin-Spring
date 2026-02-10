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
import com.example.bikerentandroid.api.LoginRequest
import com.example.bikerentandroid.api.UserUpdateDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangePasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = SessionManager.getCurrentUser(requireContext())
        if (user == null) {
            Toast.makeText(requireContext(), "Not logged in", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        val currentPasswordInput = view.findViewById<EditText>(R.id.currentPassword)
        val newPasswordInput = view.findViewById<EditText>(R.id.newPassword)
        val confirmPasswordInput = view.findViewById<EditText>(R.id.confirmPassword)
        val saveButton = view.findViewById<Button>(R.id.btnSave)

        saveButton.setOnClickListener {
            val currentPassword = currentPasswordInput.text.toString()
            val newPassword = newPasswordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            if (currentPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
                Toast.makeText(requireContext(), R.string.password_required, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(requireContext(), R.string.passwords_dont_match, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveButton.isEnabled = false
            lifecycleScope.launch {
                try {
                    // Verify current password by calling login (Android app: isAdmin = false)
                    val loginResponse = withContext(Dispatchers.IO) {
                        ApiClient.authApi.login(
                            LoginRequest(
                                username = user.username ?: "",
                                password = currentPassword,
                                isAdmin = false
                            )
                        )
                    }
                    if (!isAdded) return@launch
                    if (!loginResponse.isSuccessful) {
                        Toast.makeText(requireContext(), R.string.current_password_wrong, Toast.LENGTH_SHORT).show()
                        saveButton.isEnabled = true
                        return@launch
                    }

                    // Update password via user API
                    val updateResponse = withContext(Dispatchers.IO) {
                        ApiClient.userApi.updateUser(
                            user.id,
                            UserUpdateDto(password = newPassword)
                        )
                    }
                    if (!isAdded) return@launch
                    if (updateResponse.isSuccessful) {
                        Toast.makeText(requireContext(), R.string.password_changed, Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    } else {
                        val errorMsg = updateResponse.errorBody()?.string() ?: "Update failed"
                        Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    if (isAdded) {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${e.message ?: "Network error"}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } finally {
                    if (isAdded) saveButton.isEnabled = true
                }
            }
        }
    }
}
