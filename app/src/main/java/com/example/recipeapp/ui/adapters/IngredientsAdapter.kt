package com.example.recipeapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.data.models.ExtendedIngredient
import com.example.recipeapp.databinding.IngredientItemBinding
import kotlin.math.roundToInt

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    private var ingredients: List<ExtendedIngredient> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(ingredients: List<ExtendedIngredient>) {
        this.ingredients = ingredients
        notifyDataSetChanged()
    }

    inner class IngredientsViewHolder(private val binding: IngredientItemBinding) :
        BaseViewHolder<ExtendedIngredient>(binding.root) {
        override fun onBind(item: ExtendedIngredient) {
            binding.ingredientTitle.text = item.name.replaceFirstChar { it.uppercase() }
            binding.ingredientAmount.text = String.format("%.1f", item.amount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val view = LayoutInflater.from(parent.context)
        return IngredientsViewHolder(IngredientItemBinding.inflate(view, parent, false))
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.onBind(ingredients[position])
    }

    override fun getItemCount(): Int = this.ingredients.size
}