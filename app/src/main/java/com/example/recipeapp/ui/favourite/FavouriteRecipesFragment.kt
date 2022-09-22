package com.example.recipeapp.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.databinding.FragmentFavouriteRecipesBinding
import com.example.recipeapp.ui.adapters.OnRecipeClickListener
import com.example.recipeapp.ui.adapters.RecipeAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment() {
    private lateinit var binding: FragmentFavouriteRecipesBinding
    private val favouriteRecipesViewModel: FavouriteRecipesViewModel by viewModels()
    private val recipeAdapter = RecipeAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        setUpRecyclerView()
        observeData()
    }

    private fun setUpAdapter() {
        recipeAdapter.setOnRecipeClickListener(object : OnRecipeClickListener {
            override fun onClick(id: Int) {
                findNavController().navigate(
                    FavouriteRecipesFragmentDirections.actionFavouriteRecipesFragmentToRecipeDetailsFragment(
                        id
                    )
                )
            }
        })
    }

    private fun setUpRecyclerView() {
        binding.favRecipesRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recipeAdapter
        }
    }

    private fun observeData() {
        favouriteRecipesViewModel.apply {
            recipes.observe(viewLifecycleOwner) { recipes ->
                recipeAdapter.setData(recipes)
            }
            loading.observe(viewLifecycleOwner) { isLoading ->
                binding.prBar.isVisible = isLoading
            }
            error.observe(viewLifecycleOwner) { errorMsg ->
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            }
            noFavourites.observe(viewLifecycleOwner) { noFav ->
                binding.noFavTxt.visibility = if (noFav) View.VISIBLE else View.INVISIBLE
            }
        }
    }
}