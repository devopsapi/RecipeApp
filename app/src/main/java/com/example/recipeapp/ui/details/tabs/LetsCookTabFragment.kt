package com.example.recipeapp.ui.details.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.databinding.FragmentLetsCookTabBinding
import com.example.recipeapp.ui.adapters.StepsAdapter
import com.example.recipeapp.ui.details.RecipeDetailViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LetsCookTabFragment : Fragment() {

    private lateinit var binding: FragmentLetsCookTabBinding
    private val recipeDetailViewModel: RecipeDetailViewModel by viewModels(ownerProducer = {
        this.parentFragment as Fragment
    })
    private val stepsAdapter = StepsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLetsCookTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observeData()
    }

    private fun setUpRecyclerView() {
        binding.rvSteps.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = stepsAdapter
        }

    }

    private fun observeData() {
        recipeDetailViewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            stepsAdapter.setData(recipe.analyzedInstructions[0].steps)
        }
    }
}