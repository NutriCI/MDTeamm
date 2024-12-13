package com.example.nutlicii.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nutlicii.R
import com.example.nutlicii.data.ViewModel.ChatViewModelFactory
import com.example.nutlicii.data.model.Message
import com.example.nutlicii.data.viewmodel.ChatViewModel
import com.example.nutlicii.ui.adapter.ChatAdapter
import com.example.nutlicii.data.repository.ChatRepository
import data.Remote.NutliciiBaseApi

class FragmentChat : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var inputMessage: EditText
    private lateinit var sendButton: Button
    private lateinit var adapter: ChatAdapter
    private lateinit var chatViewModel: ChatViewModel

    private val messages = mutableListOf<Message>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_chat, container, false)

        recyclerView = binding.findViewById(R.id.recyclerView)
        inputMessage = binding.findViewById(R.id.inputMessage)
        sendButton = binding.findViewById(R.id.sendButton)

        val apiService = NutliciiBaseApi.getApiService()
        val repository = ChatRepository(apiService)
        val viewModelFactory = ChatViewModelFactory(repository)
        chatViewModel = ViewModelProvider(this, viewModelFactory).get(ChatViewModel::class.java)

        adapter = ChatAdapter(messages)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        chatViewModel.responseMessage.observe(viewLifecycleOwner) { response ->
            messages.add(Message("assistant", response))
            adapter.notifyItemInserted(messages.size - 1)
            recyclerView.scrollToPosition(messages.size - 1)
        }

        chatViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            messages.add(Message("assistant", error))
            adapter.notifyItemInserted(messages.size - 1)
            recyclerView.scrollToPosition(messages.size - 1)
        }


        sendButton.setOnClickListener {
            val userMessage = inputMessage.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                messages.add(Message("user", userMessage))
                adapter.notifyItemInserted(messages.size - 1)
                recyclerView.scrollToPosition(messages.size - 1)
                inputMessage.text.clear()
                chatViewModel.sendMessage(userMessage)
            }
        }

        return binding
    }
}
