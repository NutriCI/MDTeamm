package com.example.nutlicii.UI.View

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.nutlicii.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
            val nextActivity = if (isLoggedIn) MainActivity::class.java else LandingActivity::class.java
            val intent = Intent(this, nextActivity)
            startActivity(intent)
            finish()
        }, 3000)
    }
}
