package com.example.nutlicii.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nutlicii.R
import com.example.nutlicii.data.model.Message

class ChatAdapter(private val messages: List<Message>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount() = messages.size

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userMessageTextView: TextView = itemView.findViewById(R.id.userMessageTextView)
        private val aiMessageTextView: TextView = itemView.findViewById(R.id.aiMessageTextView)

        fun bind(message: Message) {
            if (message.role == "user") {
                userMessageTextView.visibility = View.VISIBLE
                aiMessageTextView.visibility = View.GONE
                userMessageTextView.text = message.content
            } else if (message.role == "assistant") {
                aiMessageTextView.visibility = View.VISIBLE
                userMessageTextView.visibility = View.GONE
                aiMessageTextView.text = message.content
            }
        }
    }
}
