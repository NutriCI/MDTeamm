package com.example.nutlicii.UI.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.nutlicii.R
import com.example.nutlicii.data.ViewModel.FoodViewModelFactory
import com.example.nutlicii.data.repository.FoodRepository
import com.example.nutlicii.ui.viewmodel.FoodViewModel
import data.Remote.NutliciiBaseApi
import data.local.db.AppDatabase

class UpdateFoodActivity : AppCompatActivity() {

    private lateinit var viewModel: FoodViewModel
    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activityfood_check_data)

        appDatabase = AppDatabase.getDatabase(this)
        val repository = FoodRepository(NutliciiBaseApi.getApiService(), appDatabase)
        viewModel = ViewModelProvider(this, FoodViewModelFactory(repository))
            .get(FoodViewModel::class.java)

        val foodName = intent.getStringExtra("foodName") ?: ""
        val foodCategory = intent.getStringExtra("foodCategory") ?: ""
        val foodCalories = intent.getIntExtra("calories", 0)
        val foodSugar = intent.getIntExtra("sugar", 0)
        val foodFat = intent.getIntExtra("fat", 0)
        val foodSalt = intent.getIntExtra("salt", 0)
        val foodid=intent.getIntExtra("ID", 0)
        findViewById<EditText>(R.id.et_food_name).setText(foodName)
        findViewById<EditText>(R.id.et_category).setText(foodCategory)
        findViewById<EditText>(R.id.et_calories).setText(foodCalories.toString())
        findViewById<EditText>(R.id.et_sugar).setText(foodSugar.toString())
        findViewById<EditText>(R.id.et_fat).setText(foodFat.toString())
        findViewById<EditText>(R.id.et_salt).setText(foodSalt.toString())

        findViewById<Button>(R.id.btn_save_foods).setOnClickListener {

            val updatedName = findViewById<EditText>(R.id.et_food_name).text.toString()
            val updatedCategory = findViewById<EditText>(R.id.et_category).text.toString()
            val updatedCalories = findViewById<EditText>(R.id.et_calories).text.toString().toIntOrNull()
            val updatedSugar = findViewById<EditText>(R.id.et_sugar).text.toString().toIntOrNull()
            val updatedFats = findViewById<EditText>(R.id.et_fat).text.toString().toIntOrNull()
            val updatedSalt = findViewById<EditText>(R.id.et_salt).text.toString().toIntOrNull()

            if (foodid == -1) {
                Toast.makeText(this, "Invalid Food ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.updateFood(
                id = foodid,
                nama_makanan = updatedName,
                category = updatedCategory,
                calories = updatedCalories,
                sugar = updatedSugar,
                fats = updatedFats,
                salt = updatedSalt,
                onSuccess = {
                    Toast.makeText(this, "Yeyyy berhasil di update", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HistoryActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                onError = { errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
