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
import androidx.navigation.fragment.findNavController

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

        // Input fields
        val firstNameInput = view.findViewById<EditText>(R.id.first_name)
        val lastNameInput = view.findViewById<EditText>(R.id.last_name)
        val usernameInput = view.findViewById<EditText>(R.id.etUsername)
        val emailInput = view.findViewById<EditText>(R.id.email)
        val phoneInput = view.findViewById<EditText>(R.id.phone)
        val passwordInput = view.findViewById<EditText>(R.id.etPassword)

        val registerButton = view.findViewById<Button>(R.id.btnLogin) // your Register button
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

            // TODO: Replace with real registration (API / DB)
            fakeRegisterSuccess(username)
        }

        // Navigate back to LoginFragment
        logInTextView.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun fakeRegisterSuccess(username: String) {
        // After registration, go to mapFragment and remove RegisterFragment from back stack
        findNavController().navigate(
            R.id.action_registerFragment_to_mapFragment
        )
    }
}
