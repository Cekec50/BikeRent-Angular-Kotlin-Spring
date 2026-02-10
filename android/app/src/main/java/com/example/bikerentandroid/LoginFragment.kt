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
import com.example.bikerentandroid.api.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

            loginButton.isEnabled = false
            lifecycleScope.launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        ApiClient.authApi.login(LoginRequest(username, password, isAdmin = false))
                    }
                    if (!isAdded) return@launch
                    if (response.isSuccessful) {
                        response.body()?.let { user ->
                            SessionManager.saveUser(requireContext(), user)
                        }
                        findNavController().navigate(R.id.action_loginFragment_to_mapFragment)
                    } else {
                        val errorMsg = response.errorBody()?.string() ?: "Login failed"
                        context?.let { Toast.makeText(it, errorMsg, Toast.LENGTH_SHORT).show() }
                    }
                } catch (e: Exception) {
                    if (isAdded) {
                        context?.let {
                            Toast.makeText(it, "Error: ${e.message ?: "Network error"}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } finally {
                    if (isAdded) loginButton.isEnabled = true
                }
            }
        }
        signUpTextView.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
    }
}
