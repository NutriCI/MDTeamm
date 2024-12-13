package com.example.nutlicii.UI.View
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nutlicii.R
import com.example.nutlicii.data.repository.FoodRepository
import com.example.nutlicii.ui.viewmodel.FoodViewModel
import data.Remote.NutliciiBaseApi
import data.local.db.AppDatabase
import java.util.Date
class FoodAddActivity : AppCompatActivity() {

    private lateinit var foodViewModel: FoodViewModel
    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activityfood_add)
        appDatabase = AppDatabase.getDatabase(this)
        val foodRepository = FoodRepository(NutliciiBaseApi.getApiService(), appDatabase)
        foodViewModel = FoodViewModel(foodRepository)

        val etFoodName = findViewById<EditText>(R.id.et_food_name)
        val etFoodCategory = findViewById<EditText>(R.id.et_category)
        val etFoodCalories = findViewById<EditText>(R.id.et_calories)
        val etFoodSugar = findViewById<EditText>(R.id.et_sugar)
        val etFoodFat = findViewById<EditText>(R.id.et_fat)
        val etFoodSalt = findViewById<EditText>(R.id.et_salt)
        val btnSaveFood = findViewById<Button>(R.id.btn_save_foods)
        val calories = intent.getIntExtra("calories", 0)
        val fat = intent.getIntExtra("fat", 0)
        val salt = intent.getIntExtra("salt", 0)
        val sugar = intent.getIntExtra("sugar", 0)
        val foodName = intent.getStringExtra("foodName")
        val foodCategory = intent.getStringExtra("foodCategory")
        etFoodCalories.setText(calories.toString())
        etFoodSugar.setText(sugar.toString())
        etFoodFat.setText(fat.toString())
        etFoodSalt.setText(salt.toString())

        foodName?.let { etFoodName.setText(it) }
        foodCategory?.let { etFoodCategory.setText(it) }

        btnSaveFood.setOnClickListener {
            val foodName = etFoodName.text.toString()
            val foodCategory = etFoodCategory.text.toString()
            val foodCalories = etFoodCalories.text.toString().toIntOrNull() ?: 0
            val foodSugar = etFoodSugar.text.toString().toIntOrNull() ?: 0
            val foodFat = etFoodFat.text.toString().toIntOrNull() ?: 0
            val foodSalt = etFoodSalt.text.toString().toIntOrNull() ?: 0

            foodViewModel.addFood(
                nama_makanan = foodName,
                category = foodCategory,
                calories = foodCalories,
                sugar = foodSugar,
                fats = foodFat,
                salt = foodSalt,
                onSuccess = {
                    Toast.makeText(this, "Yeee berhasil!!!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@FoodAddActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                onError = { error ->
                    Toast.makeText(this, "Duhh,ada kesalahan nih...", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@FoodAddActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            )
        }
    }
}
