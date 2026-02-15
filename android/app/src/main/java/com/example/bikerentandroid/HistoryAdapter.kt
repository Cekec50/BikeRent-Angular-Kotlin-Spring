package com.example.bikerentandroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bikerentandroid.model.History

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var items: List<History> = emptyList()

    fun submitList(newItems: List<History>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeText: TextView = itemView.findViewById(R.id.historyItemType)
        private val priceText: TextView = itemView.findViewById(R.id.historyItemPrice)
        private val dateText: TextView = itemView.findViewById(R.id.historyItemDate)
        private val durationText: TextView = itemView.findViewById(R.id.historyItemDuration)

        fun bind(history: History) {
            typeText.text = history.getBikeTypeDisplay()
            priceText.text = history.getPriceDisplay()
            dateText.text = history.getDateDisplay()
            durationText.text = history.getDurationDisplay()
        }
    }
}
