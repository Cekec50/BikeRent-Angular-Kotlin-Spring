package com.example.bikerentandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bikerentandroid.api.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = SessionManager.getCurrentUser(requireContext())
        if (user == null) {
            Toast.makeText(requireContext(), "Not logged in", Toast.LENGTH_SHORT).show()
            activity?.onBackPressedDispatcher?.onBackPressed()
            return
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.historyRecyclerView)
        val emptyMessage = view.findViewById<TextView>(R.id.emptyMessage)

        val adapter = HistoryAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.userApi.getHistory(user.id)
                }
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    adapter.submitList(list)
                    emptyMessage.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Failed to load history"
                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                    emptyMessage.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                context?.let {
                    Toast.makeText(
                        it,
                        "Error: ${e.message ?: "Network error"}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                emptyMessage.visibility = View.VISIBLE
            }
        }
    }
}
