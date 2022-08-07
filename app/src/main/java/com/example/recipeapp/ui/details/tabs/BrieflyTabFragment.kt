package com.example.recipeapp.ui.details.tabs

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.recipeapp.databinding.FragmentBrieflyTabBinding
import com.example.recipeapp.ui.details.RecipeDetailViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BrieflyTabFragment : Fragment() {

    private lateinit var binding: FragmentBrieflyTabBinding
    private val recipeDetailViewModel: RecipeDetailViewModel by viewModels(
        ownerProducer = { this.parentFragment as Fragment })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBrieflyTabBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.instruction.movementMethod = ScrollingMovementMethod()
        observeData()
    }

    private fun observeData() {
        recipeDetailViewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            binding.recipeTitle.text = recipe.title
            binding.timeForCook.text = recipe.readyInMinutes.toString() + " min"
            binding.likes.text = recipe.aggregateLikes.toString()
            val instructions = HtmlCompat.fromHtml(
                recipe.instructions,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()
            binding.instruction.text = instructions.ifEmpty { "No instructions here" }
        }
    }
}