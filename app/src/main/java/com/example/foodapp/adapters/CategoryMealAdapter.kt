package com.example.foodapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.MealItemBinding
import com.example.foodapp.pojo.MealByCategory

class CategoryMealAdapter : RecyclerView.Adapter<CategoryMealAdapter.CategoryMealViewHolder>() {
    private var mealList = ArrayList<MealByCategory>()

    @SuppressLint("NotifyDataSetChanged")
    fun setMealList(mealList: List<MealByCategory>) {
        this.mealList = mealList as ArrayList<MealByCategory>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealViewHolder {
        return CategoryMealViewHolder(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = mealList[position].strMeal
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    inner class CategoryMealViewHolder(val binding: MealItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}