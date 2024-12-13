// LoginActivity.kt
package com.example.nutlicii.UI.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.nutlicii.R
import com.example.nutlicii.data.ViewModel.AuthViewModel
import com.example.nutlicii.data.ViewModel.AuthViewModelFactory
import com.example.nutlicii.data.repository.AuthRepository
import data.local.db.AppDatabase
import data.Remote.NutliciiBaseApi
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepository(NutliciiBaseApi.getApiService(), AppDatabase.getDatabase(this)))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)

        btnSignIn.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.login(username, password)
            } else {
                Toast.makeText(this, "hmmm,isi dulu semua!!!!", Toast.LENGTH_SHORT).show()
            }
        }

        observeLoginResult()
    }

    private fun observeLoginResult() {
        loginViewModel.loginResult.observe(this) { response ->
            if (response.isSuccessful) {
                val user = response.body()?.data
                if (user != null) {
                    showMessage("Asikkkk,berhasil login!!!")
                    lifecycleScope.launch {
                        loginViewModel.saveUser(user)
                        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("user_data", user)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    showMessage("Terjadi kesalahan nih.")
                }
            } else {
                showMessage("Terjadi kesalahan nih.")
            }
        }
    }
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
