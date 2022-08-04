package com.example.recipeapp.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.recipeapp.ui.details.tabs.BrieflyTabFragment
import com.example.recipeapp.ui.details.tabs.IngredientsTabFragment
import com.example.recipeapp.ui.details.tabs.LetsCookTabFragment

class RecipeDetailTabPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabsAmount = 3

    override fun getItemCount(): Int = tabsAmount

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IngredientsTabFragment()
            1 -> BrieflyTabFragment()
            2 -> LetsCookTabFragment()
            else -> IngredientsTabFragment()
        }
    }
}