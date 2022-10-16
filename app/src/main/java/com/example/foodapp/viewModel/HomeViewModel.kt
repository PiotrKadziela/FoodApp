package com.example.foodapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.db.MealDB
import com.example.foodapp.pojo.*
import com.example.foodapp.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val mealDB: MealDB) : ViewModel() {
    private val categoriesLiveData = MutableLiveData<List<Category>>()
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealByCategory>>()
    private var favoriteMealsLiveData = mealDB.mealDao().getAll()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private var searchedMealsLiveData = MutableLiveData<List<Meal>>()

    init {
        getRandomMeal()
    }

    private fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        })
    }

    fun getPopularItems() {
        RetrofitInstance.api.getPoularItems("Seafood")
            .enqueue(object : Callback<MealByCategoryList> {
                override fun onResponse(
                    call: Call<MealByCategoryList>,
                    response: Response<MealByCategoryList>
                ) {
                    response.body()?.let {
                        popularItemsLiveData.value = it.meals
                    }
                }

                override fun onFailure(call: Call<MealByCategoryList>, t: Throwable) {
                    Log.d("HomeViewModel", t.message.toString())
                }

            })
    }

    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let {
                    categoriesLiveData.value = it.categories
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }

        })
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDB.mealDao().delete(meal)
        }
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDB.mealDao().update(meal)
        }
    }

    fun getMealById(id: String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let {
                    bottomSheetMealLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }

        })
    }

    fun searchMeals(searchQuery: String) = RetrofitInstance.api.searchMeals(searchQuery).enqueue(
        object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealsList = response.body()?.meals
                mealsList?.let {
                    searchedMealsLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }

        }
    )

    fun observeRandomMealLiveData(): LiveData<Meal> = randomMealLiveData
    fun observeSearchedMealsLiveData(): LiveData<List<Meal>> = searchedMealsLiveData
    fun observePopularItemsLiveData(): LiveData<List<MealByCategory>> = popularItemsLiveData
    fun observeCategoriesLiveData(): LiveData<List<Category>> = categoriesLiveData
    fun observeFavoriteMealsLiveData(): LiveData<List<Meal>> = favoriteMealsLiveData
    fun observeBottomSheetMealLiveData(): LiveData<Meal> = bottomSheetMealLiveData
}