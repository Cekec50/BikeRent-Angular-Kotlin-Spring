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
import com.example.bikerentandroid.api.UserUpdateDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = SessionManager.getCurrentUser(requireContext())
        if (user == null) {
            Toast.makeText(requireContext(), "Not logged in", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        val firstNameInput = view.findViewById<EditText>(R.id.firstname)
        val lastNameInput = view.findViewById<EditText>(R.id.lastname)
        val usernameInput = view.findViewById<EditText>(R.id.username)
        val emailInput = view.findViewById<EditText>(R.id.email)
        val phoneInput = view.findViewById<EditText>(R.id.phone)
        val saveButton = view.findViewById<Button>(R.id.btnSave)

        firstNameInput.setText(user.firstName ?: "")
        lastNameInput.setText(user.lastName ?: "")
        usernameInput.setText(user.username ?: "")
        emailInput.setText(user.email ?: "")
        phoneInput.setText(user.phone ?: "")

        saveButton.setOnClickListener {
            val firstName = firstNameInput.text.toString().trim()
            val lastName = lastNameInput.text.toString().trim()
            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()

            if (username.isEmpty()) {
                Toast.makeText(requireContext(), "Username is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveButton.isEnabled = false
            lifecycleScope.launch {
                try {
                    val dto = UserUpdateDto(
                        username = username,
                        password = null,
                        firstName = firstName.ifEmpty { null },
                        lastName = lastName.ifEmpty { null },
                        phone = phone.ifEmpty { null },
                        email = email.ifEmpty { null }
                    )
                    val response = withContext(Dispatchers.IO) {
                        ApiClient.userApi.updateUser(user.id, dto)
                    }
                    if (!isAdded) return@launch
                    if (response.isSuccessful) {
                        response.body()?.let { updatedUser ->
                            SessionManager.saveUser(requireContext(), updatedUser)
                        }
                        Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    } else {
                        val errorMsg = response.errorBody()?.string() ?: "Update failed"
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
