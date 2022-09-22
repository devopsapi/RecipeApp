package com.example.recipeapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentRecipeDetailsBinding
import com.example.recipeapp.ui.adapters.RecipeDetailTabPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailsFragment : Fragment() {

    private lateinit var binding: FragmentRecipeDetailsBinding
    private val recipeDetailViewModel: RecipeDetailViewModel by viewModels()
    private val safeArgs: RecipeDetailsFragmentArgs by navArgs()
    private val tabTitles = arrayListOf("INGREDIENTS", "BRIEFLY", "LET'S COOK")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeDetailViewModel.getRecipeById(safeArgs.recipeId.toLong())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnClose.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnFavourite.setOnClickListener {
            recipeDetailViewModel.setFavorite(safeArgs.recipeId)
        }

        setUpViewPager()
        observeData()
    }

    private fun setUpViewPager() {
        binding.viewPager.adapter = RecipeDetailTabPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()
        for (i in 0..2) {
            val textView =
                LayoutInflater.from(requireContext()).inflate(R.layout.tab_title, null) as TextView
            textView.text = tabTitles[i]
            binding.tabLayout.getTabAt(i)?.customView = textView
        }
    }

    private fun observeData() {
        recipeDetailViewModel.apply {
            recipe.observe(viewLifecycleOwner) { recipe ->
                Glide.with(requireContext())
                    .load(recipe.image)
                    .error(R.drawable.ic_blank_photo)
                    .into(binding.recipeImg)
            }
            isFavourite.observe(viewLifecycleOwner) { isFav ->
                if (isFav) binding.btnFavourite.setImageResource(R.drawable.ic_full_heart)
                else binding.btnFavourite.setImageResource(R.drawable.ic_empty_heart)
            }
            error.observe(viewLifecycleOwner) { errorMsg ->
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}