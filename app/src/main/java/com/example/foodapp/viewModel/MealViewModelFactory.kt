package com.example.foodapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.db.MealDB

@Suppress("UNCHECKED_CAST")
class MealViewModelFactory(private val mealDB: MealDB) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealViewModel(mealDB) as T
    }
}