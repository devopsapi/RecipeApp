package com.example.recipeapp.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentHomeBinding
import com.example.recipeapp.ui.adapters.OnRecipeClickListener
import com.example.recipeapp.ui.adapters.RecipeAdapter
import com.example.recipeapp.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val recipeAdapter = RecipeAdapter()
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility =
            View.VISIBLE

        setUpLogoutBtn()
        setUpAdapter()
        setUpRecyclerView()
        observeData()
        setUpSwipeLayout()
        setUpSearch()
    }

    private fun setUpLogoutBtn() {
        binding.logout.setOnClickListener {
            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putBoolean(Constants.IS_LOGGED_KEY, false)
                apply()
            }
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
        }
    }

    private fun setUpSearch() {
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 != null) {
                    binding.searchView.isFocusable = false
                    homeViewModel.getSpecificRecipe(p0)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
    }

    private fun setUpSwipeLayout() {
        binding.swipeLayout.apply {
            setOnRefreshListener {
                if (binding.searchView.query.isNullOrEmpty()) {
                    homeViewModel.getRecipes()
                } else {
                    homeViewModel.getSpecificRecipe(binding.searchView.query.toString())
                }
                isRefreshing = false
            }
        }
    }

    private fun setUpAdapter() {
        recipeAdapter.setOnRecipeClickListener(object : OnRecipeClickListener {
            override fun onClick(id: Int) {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToRecipeDetailsFragment(
                        id
                    )
                )
            }
        })
    }

    private fun setUpRecyclerView() {
        binding.rvRecipes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recipeAdapter
        }
    }

    private fun observeData() {
        homeViewModel.apply {
            recipes.observe(viewLifecycleOwner) { recipes ->
                recipeAdapter.setData(recipes)
            }
            progressBar.observe(viewLifecycleOwner) { isLoading ->
                binding.prBar.isVisible = isLoading
            }
            error.observe(viewLifecycleOwner) { errorMsg ->
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}