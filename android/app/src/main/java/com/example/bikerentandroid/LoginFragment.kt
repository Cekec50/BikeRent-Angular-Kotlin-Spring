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

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameInput = view.findViewById<EditText>(R.id.etUsername)
        val passwordInput = view.findViewById<EditText>(R.id.etPassword)
        val loginButton = view.findViewById<Button>(R.id.btnLogin)
        val signUpTextView = view.findViewById<TextView>(R.id.signUpTextView)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter username and password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // TODO: Replace with real authentication (API / DB)
            fakeLoginSuccess(username)
        }
        // Navigate to registrationFragment on click
        signUpTextView.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
    }

    private fun fakeLoginSuccess(username: String) {
        // Later you can store username in SharedPreferences or ViewModel
        findNavController().navigate(
            R.id.action_loginFragment_to_mapFragment
        )
    }
}
