package com.example.foodapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.adapters.CategoryMealAdapter
import com.example.foodapp.databinding.ActivityCategoryBinding
import com.example.foodapp.fragments.HomeFragment
import com.example.foodapp.viewModel.CategoryViewModel

class CategoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityCategoryBinding
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var categoryMealAdapter: CategoryMealAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        val categoryName = intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!
        categoryViewModel.getMealsByCategory(categoryName)
        categoryViewModel.observeMealsLiveData().observe(this) { mealList ->
            binding.tvCategoryCount.text = "$categoryName : ${mealList.size}"
            categoryMealAdapter.setMealList(mealList)
        }

        onMealClick()
    }

    private fun onMealClick() {
        categoryMealAdapter.onItemClick = { meal ->
            val intent = Intent(this, MealActivity::class.java)
            intent.apply {
                putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
                putExtra(HomeFragment.MEAL_ID, meal.idMeal)
                putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            }
            startActivity(intent)
        }
    }

    private fun prepareRecyclerView() {
        categoryMealAdapter = CategoryMealAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealAdapter
        }
    }
}