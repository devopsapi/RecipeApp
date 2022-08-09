package com.example.recipeapp.ui.details.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.databinding.FragmentIngredientsTabBinding
import com.example.recipeapp.ui.adapters.IngredientsAdapter
import com.example.recipeapp.ui.details.RecipeDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IngredientsTabFragment : Fragment() {

    private lateinit var binding: FragmentIngredientsTabBinding
    private val recipeDetailViewModel: RecipeDetailViewModel by viewModels(
        ownerProducer = { this.parentFragment as Fragment })
    private val ingredientAdapter = IngredientsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIngredientsTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observeData()
        setUpCalculateBtn()
    }

    private fun setUpCalculateBtn() {
        binding.calculateBtn.setOnClickListener {
            recipeDetailViewModel.calculateNewCaloriesAndIngredientsAmount(
                binding.servingsAmount.text.toString().toInt()
            )
        }
    }

    private fun setUpRecyclerView() {
        binding.ingredientsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ingredientAdapter
        }
    }

    private fun observeData() {
        recipeDetailViewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            binding.servingsAmount.setText(recipe.servings.toString())
        }
        recipeDetailViewModel.calculatedIngredient.observe(viewLifecycleOwner) { calculatedIngredient ->
            binding.caloriesAmount.text = calculatedIngredient.caloriesAmount.toString()
            ingredientAdapter.setData(calculatedIngredient.extendedIngredient)
        }
    }
}