package com.example.nutlicii.UI.View

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nutlicii.R
import com.example.nutlicii.UI.Adapter.HistoryAdapter
import com.example.nutlicii.data.ViewModel.HistoryViewModel
import com.example.nutlicii.data.ViewModel.HistoryViewModelFactory
import com.example.nutlicii.data.model.FoodItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import data.Remote.NutliciiBaseApi
import data.local.db.AppDatabase
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity : AppCompatActivity() {

    private lateinit var datesContainer: LinearLayout
    private lateinit var historyRecyclerView: RecyclerView
    private var currentDate: Calendar = Calendar.getInstance()
    private var startDate: Calendar = currentDate.clone() as Calendar
    private val dateFormat = SimpleDateFormat("EEE", Locale.getDefault())
    private val dayFormat = SimpleDateFormat("dd", Locale.getDefault())
    private var selectedIndex = 0
    private var lastSelectedView: LinearLayout? = null

    private val historyViewModel: HistoryViewModel by viewModels {
        val userDao = AppDatabase.getDatabase(applicationContext).userDao()
        val apiService = NutliciiBaseApi.getApiService()
        HistoryViewModelFactory(application, apiService, userDao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        datesContainer = findViewById(R.id.datesContainer)
        historyRecyclerView = findViewById(R.id.history_recycler_view)
        val btnAddFood: FloatingActionButton = findViewById(R.id.food_add)

        btnAddFood.setOnClickListener {
            val intent = Intent(this, FoodAddActivity::class.java)
            startActivity(intent)
            finish()
        }
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyViewModel.historyItems.observe(this, Observer { historyList ->
            val foodItems = historyList.map { historyItem ->
                FoodItem(
                    date_added = historyItem.date_added,
                    nama_makanan = historyItem.nama_makanan,
                    category = historyItem.category,
                    grade = historyItem.grade,
                    calories = historyItem.calories,
                    sugar = historyItem.sugar,
                    fats = historyItem.fats,
                    salt = historyItem.salt,
                    id = historyItem.id
                )
            }
            val adapter = HistoryAdapter(foodItems)
            historyRecyclerView.adapter = adapter
        })

        selectedIndex = currentDate.get(Calendar.DAY_OF_MONTH) - startDate.get(Calendar.DAY_OF_MONTH)
        updateDates()

        findViewById<ImageButton>(R.id.prev_arrow).setOnClickListener {
            shiftDay(-1)
        }

        findViewById<ImageButton>(R.id.next_arrow).setOnClickListener {
            shiftDay(1)
        }
    }

    private fun updateDates() {
        datesContainer.removeAllViews()
        val tempCalendar = startDate.clone() as Calendar

        for (i in 0 until 7) {
            val dateLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = android.view.Gravity.CENTER
                setPadding(12, 6, 12, 6)
            }

            val dayView = TextView(this).apply {
                text = dateFormat.format(tempCalendar.time)
                gravity = android.view.Gravity.CENTER
                setTextSize(14f)
                setTextColor(android.graphics.Color.GRAY)
            }

            val dateView = TextView(this).apply {
                text = dayFormat.format(tempCalendar.time)
                gravity = android.view.Gravity.CENTER
                setTextSize(16f)
                setTextColor(android.graphics.Color.BLACK)
                setOnClickListener {
                    applySelectionStyle(dateLayout, i)
                }
            }

            dateLayout.addView(dayView)
            dateLayout.addView(dateView)
            datesContainer.addView(dateLayout)

            if (i == selectedIndex) {
                applySelectionStyle(dateLayout, i)
            }

            tempCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    private fun shiftDay(days: Int) {
        selectedIndex += days

        if (selectedIndex >= 7) {
            startDate.add(Calendar.DAY_OF_MONTH, 7)
            selectedIndex = 0
        } else if (selectedIndex < 0) {
            startDate.add(Calendar.DAY_OF_MONTH, -7)
            selectedIndex = 6
        }

        updateDates()

        val newSelectedView = datesContainer.getChildAt(selectedIndex) as LinearLayout
        applySelectionStyle(newSelectedView, selectedIndex)
    }

    private fun applySelectionStyle(view: LinearLayout, index: Int) {
        lastSelectedView?.setBackgroundResource(0)
        view.setBackgroundResource(R.drawable.date_selected_background)
        lastSelectedView = view

        selectedIndex = index
        val selectedDate = getSelectedDate()
        historyViewModel.getHistoryItemsByDate(selectedDate)
    }

    private fun getSelectedDate(): String {
        val tempCalendar = startDate.clone() as Calendar
        tempCalendar.add(Calendar.DAY_OF_MONTH, selectedIndex)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(tempCalendar.time)
    }
}
