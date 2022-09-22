package com.example.recipeapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.models.Recipe
import com.example.recipeapp.databinding.RecipeItemBinding

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private var recipes = ArrayList<Recipe>()
    private lateinit var onRecipeClickListener: OnRecipeClickListener

    @SuppressLint("NotifyDataSetChanged")
    fun setData(recipes: List<Recipe>) {
        this.recipes.clear()
        this.recipes.addAll(recipes)
        this.notifyDataSetChanged()
    }

    fun setOnRecipeClickListener(onRecipeClickListener: OnRecipeClickListener) {
        this.onRecipeClickListener = onRecipeClickListener
    }

    inner class RecipeViewHolder(private val itemBinding: RecipeItemBinding) :
        BaseViewHolder<Recipe>(itemBinding.root) {
        @SuppressLint("SetTextI18n")
        override fun onBind(item: Recipe) {
            itemBinding.apply {
                recipeTitle.text = item.title
                timeForCook.text = "${item.readyInMinutes} + min"

                Glide.with(itemBinding.root.context)
                    .load(item.image)
                    .error(R.drawable.ic_blank_photo)
                    .into(itemBinding.recipeImg)

                root.setOnClickListener {
                    onRecipeClickListener.onClick(item.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
        return RecipeViewHolder(RecipeItemBinding.inflate(view, parent, false))
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.onBind(this.recipes[position])
    }

    override fun getItemCount(): Int = this.recipes.size
}

interface OnRecipeClickListener {
    fun onClick(id: Int)
}