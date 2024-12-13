package com.example.nutlicii.UI.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.nutlicii.R
import com.example.nutlicii.data.repository.FoodRepository
import com.example.nutlicii.ui.viewmodel.FoodViewModel
import com.example.nutlicii.data.ViewModel.FoodViewModelFactory
import data.Remote.NutliciiBaseApi
import data.local.db.AppDatabase

class DetailsFoodActivity : AppCompatActivity() {
    private lateinit var viewModel: FoodViewModel
    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activityfood_detail_food)

        // Set up ViewModel
        appDatabase = AppDatabase.getDatabase(this)
        val repository = FoodRepository(NutliciiBaseApi.getApiService(), appDatabase)
        viewModel = ViewModelProvider(this, FoodViewModelFactory(repository))
            .get(FoodViewModel::class.java)

        val foodName = intent.getStringExtra("EXTRA_NAME")
        val foodCategory = intent.getStringExtra("EXTRA_CATEGORY")
        val foodCalories = intent.getStringExtra("EXTRA_CALORIES")
        val foodSugar = intent.getStringExtra("EXTRA_SUGAR")
        val foodFat = intent.getStringExtra("EXTRA_FAT")
        val foodSalt = intent.getStringExtra("EXTRA_SALT")
        val foodGrade = intent.getStringExtra("EXTRA_GRADE")
        val foodid = intent.getIntExtra("ID", -1)

        findViewById<TextView>(R.id.title).text = "Details Food"
        findViewById<TextView>(R.id.food_name).text = foodName
        findViewById<TextView>(R.id.food_category).text = foodCategory
        findViewById<TextView>(R.id.food_calories).text = foodCalories
        findViewById<TextView>(R.id.food_sugar).text = foodSugar
        findViewById<TextView>(R.id.food_fat).text = foodFat
        findViewById<TextView>(R.id.food_salt).text = foodSalt
        findViewById<TextView>(R.id.grade_value).text = foodGrade

        findViewById<Button>(R.id.edit_button).setOnClickListener {
            val intent = Intent(this, UpdateFoodActivity::class.java)
            intent.putExtra("foodName", foodName)
            intent.putExtra("foodCategory", foodCategory)
            intent.putExtra("calories", foodCalories?.toIntOrNull() ?: 0)
            intent.putExtra("sugar", foodSugar?.toIntOrNull() ?: 0)
            intent.putExtra("fat", foodFat?.toIntOrNull() ?: 0)
            intent.putExtra("salt", foodSalt?.toIntOrNull() ?: 0)
            intent.putExtra("ID", foodid)
            startActivity(intent)
        }

        findViewById<Button>(R.id.delete_button).setOnClickListener {
            if (foodid != -1) {
                viewModel.deleteFood(
                    id = foodid,
                    onSuccess = {
                        Toast.makeText(this, "Yeahh,Data kamu berhasil di hapuss", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HistoryActivity::class.java)
                        startActivity(intent)
                        finish()
                    },
                    onError = { errorMessage ->
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Toast.makeText(this, "Ada yang salah nihhh", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
