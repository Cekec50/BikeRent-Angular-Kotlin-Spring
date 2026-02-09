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

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editProfileTextView = view.findViewById<TextView>(R.id.editProfileTextView)
        val historyTextView = view.findViewById<TextView>(R.id.historyTextView)
        val logOutTextView = view.findViewById<TextView>(R.id.logOutTextView)


        // Navigate to registrationFragment on click
        editProfileTextView.setOnClickListener {
            findNavController().navigate(R.id.editProfileFragment)
        }
        // Navigate to registrationFragment on click
        historyTextView.setOnClickListener {
            findNavController().navigate(R.id.historyFragment)
        }
        // Navigate to registrationFragment on click
        logOutTextView.setOnClickListener {
            //TODO: Clear data if needed
            findNavController().navigate(R.id.action_logout)
        }
    }

}
