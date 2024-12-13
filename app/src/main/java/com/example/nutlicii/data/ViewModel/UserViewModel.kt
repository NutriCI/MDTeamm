package com.example.nutlicii.data.ViewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutlicii.data.Repository.UserRepository
import com.example.nutlicii.data.model.DashboardResponse
import com.example.nutlicii.data.utils.Result
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _dashboardData = MutableLiveData<Result<DashboardResponse>>()
    val dashboardData: LiveData<Result<DashboardResponse>> get() = _dashboardData

    // This method launches a coroutine and calls the suspend function from the repository
    fun fetchDashboardData() {
        // Call the suspend function from the repository in a coroutine
        viewModelScope.launch {
            // You can get the current date from here or pass it as a parameter
            val result = userRepository.getDashboardData()
            _dashboardData.value = result.value
        }
    }
}
