package com.example.nutlicii.data.ViewModel;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.nutlicii.data.model.ApiResponse
import com.example.nutlicii.data.repository.AuthRepository
import data.model.Userdata
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel(private val AuthRepository: AuthRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Response<ApiResponse<Userdata>>>()
    val loginResult: LiveData<Response<ApiResponse<Userdata>>> = _loginResult
    private val _registerResult = MutableLiveData<Response<ApiResponse<Userdata>>>()
    val registerResult: LiveData<Response<ApiResponse<Userdata>>> = _registerResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val response = AuthRepository.login(username, password)
            _loginResult.postValue(response)
        }
    }
    fun registerUser(username: String, password: String, email: String,name:String,repeatPassword:String) {
        viewModelScope.launch {
            val response = AuthRepository.register(username, password, email, name, repeatPassword)
            _registerResult.postValue(response)
        }
    }
    fun saveUser(user: Userdata) {
        viewModelScope.launch {
            AuthRepository.saveUserToDb(user)
        }
    }
    fun Logout(username: String) {
        viewModelScope.launch {
            AuthRepository.Delete(username)
        }
    }
}
