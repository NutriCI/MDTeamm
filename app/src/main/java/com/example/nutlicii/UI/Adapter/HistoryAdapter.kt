package com.example.nutlicii.UI.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nutlicii.R
import com.example.nutlicii.UI.View.DetailsFoodActivity
import com.example.nutlicii.data.model.FoodItem

class HistoryAdapter(private val items: List<FoodItem>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view, parent.context)
    }
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]
        holder.date.text = item.date_added
        holder.title.text = item.nama_makanan
        holder.type.text = item.category
        holder.icon.text = item.grade
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.context, DetailsFoodActivity::class.java).apply {
                putExtra("EXTRA_NAME", item.nama_makanan)
                putExtra("EXTRA_CATEGORY", item.category)
                putExtra("EXTRA_CALORIES", item.calories.toString())
                putExtra("EXTRA_SUGAR", item.sugar.toString())
                putExtra("EXTRA_FAT", item.fats.toString())
                putExtra("EXTRA_SALT", item.salt.toString())
                putExtra("EXTRA_GRADE", item.grade)
                putExtra("ID", item.id)
            }
            holder.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
    class HistoryViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.findViewById(R.id.textViewDate)
        val title: TextView = view.findViewById(R.id.textViewTitle)
        val type: TextView = view.findViewById(R.id.textViewDrink)
        val icon: TextView = view.findViewById(R.id.textViewStatus)
    }
}
