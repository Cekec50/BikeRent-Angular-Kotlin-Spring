package com.example.bikerentandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bikerentandroid.api.ApiClient
import com.example.bikerentandroid.api.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firstNameInput = view.findViewById<EditText>(R.id.first_name)
        val lastNameInput = view.findViewById<EditText>(R.id.last_name)
        val usernameInput = view.findViewById<EditText>(R.id.etUsername)
        val emailInput = view.findViewById<EditText>(R.id.email)
        val phoneInput = view.findViewById<EditText>(R.id.phone)
        val passwordInput = view.findViewById<EditText>(R.id.etPassword)
        val registerButton = view.findViewById<Button>(R.id.btnLogin)
        val logInTextView = view.findViewById<TextView>(R.id.logInTextView)

        registerButton.setOnClickListener {
            val firstName = firstNameInput.text.toString().trim()
            val lastName = lastNameInput.text.toString().trim()
            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() ||
                email.isEmpty() || phone.isEmpty() || password.isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            registerButton.isEnabled = false
            lifecycleScope.launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        ApiClient.authApi.register(
                            RegisterRequest(firstName, lastName, username, email, phone, password, isAdmin = false)
                        )
                    }
                    if (!isAdded) return@launch
                    if (response.isSuccessful) {
                        findNavController().navigate(R.id.action_registerFragment_to_mapFragment)
                    } else {
                        val errorMsg = response.errorBody()?.string() ?: "Registration failed"
                        context?.let { Toast.makeText(it, errorMsg, Toast.LENGTH_SHORT).show() }
                    }
                } catch (e: Exception) {
                    if (isAdded) {
                        context?.let {
                            Toast.makeText(it, "Error: ${e.message ?: "Network error"}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } finally {
                    if (isAdded) registerButton.isEnabled = true
                }
            }
        }

        logInTextView.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }
}
