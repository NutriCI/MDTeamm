package com.example.nutlicii.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.nutlicii.R
import com.example.nutlicii.UI.View.HistoryActivity
import com.example.nutlicii.data.Repository.UserRepository
import com.example.nutlicii.data.ViewModel.UserViewModel
import com.example.nutlicii.data.ViewModel.UserViewModelFactory
import com.example.nutlicii.data.utils.Result
import data.Remote.NutliciiBaseApi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = NutliciiBaseApi.getApiService()
        val userRepository = UserRepository(requireContext(), apiService)
        val factory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        val percentageView: TextView = view.findViewById(R.id.percentage)
        val progresNumber: TextView = view.findViewById(R.id.progresNumber)
        val sugarTextView: TextView = view.findViewById(R.id.sugarNumber)
        val fatTextView: TextView = view.findViewById(R.id.fatNumber)
        val saltTextView: TextView = view.findViewById(R.id.saltNumber)
        val bmiTextView: TextView = view.findViewById(R.id.bmiNumber)
        val adviceTextView: TextView = view.findViewById(R.id.adviceTextView)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val progressBar5: ProgressBar = view.findViewById(R.id.progressBar5)
        val historybtn: CardView = view.findViewById(R.id.card_icon_button)
        val date:TextView=view.findViewById(R.id.date)
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        date.text = currentDate
        historybtn.setOnClickListener {
            val intent = Intent(requireContext(), HistoryActivity::class.java)
            startActivity(intent)
        }

        userViewModel.dashboardData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    val dashboardData = result.data
                    val data = dashboardData.data

                    val percentage = data.progress_percentage
                    val caloriesCurrent = data.daily_calories
                    val caloriesGoal = data.calories_goal
                    val sugar = data.daily_sugar
                    val fat = data.daily_salt
                    val salt = data.daily_fat
                    val bmi = data.bmi
                    val advice = data.advices

                    percentageView.text = "$percentage%"
                    progresNumber.text = "$caloriesCurrent"
                    sugarTextView.text = "$sugar"
                    fatTextView.text = "$fat"
                    saltTextView.text = "$salt"
                    bmiTextView.text = "$bmi"
                    adviceTextView.text = advice

                    progressBar.visibility = View.GONE
                    progressBar5.max = caloriesGoal
                    progressBar5.progress = caloriesCurrent
                }
                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    percentageView.text = "0"
                    progresNumber.text = "0"
                    sugarTextView.text = "0"
                    fatTextView.text = "0"
                    saltTextView.text = "0"
                    bmiTextView.text = "0"
                    adviceTextView.text = "Makan Apa yang sehat tapi enak????"
                    progressBar5.max = 0
                }
            }
        })
        userViewModel.fetchDashboardData()
    }
}
