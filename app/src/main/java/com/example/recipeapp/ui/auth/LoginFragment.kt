package com.example.recipeapp.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentLoginBinding
import com.example.recipeapp.ui.util.Routes
import com.example.recipeapp.ui.util.UiEvent
import com.example.recipeapp.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (checkIfLogged()) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
        }

        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility =
            View.INVISIBLE

        binding.signUpTxt.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
        }

        binding.loginBtn.setOnClickListener {
            if (correctFields()) {
                authViewModel.loginUser(
                    binding.emailEt.text.toString(),
                    binding.passwordEt.text.toString()
                )
            }
        }

        observeData()
    }

    private fun observeData() {
        authViewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                is UiEvent.Loading -> {
                    binding.loginBtn.isEnabled = !uiEvent.isLoading
                    binding.loginBtn.background =
                        if (uiEvent.isLoading) resources.getDrawable(R.drawable.disabled_btn) else resources.getDrawable(
                            R.drawable.round_violet_btn
                        )
                    binding.progressBar.visibility =
                        if (uiEvent.isLoading) View.VISIBLE else View.INVISIBLE
                }
                is UiEvent.Navigate -> {
                    stayLogged()
                    navigateToRoute(uiEvent.route)
                }
                is UiEvent.ShowToast -> Toast.makeText(context, uiEvent.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun navigateToRoute(route: String) {
        when (route) {
            Routes.NAVIGATE_TO_HOME -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
            Routes.NAVIGATE_TO_REGISTRATION -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
            else -> return
        }
    }

    private fun checkIfLogged(): Boolean {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean(Constants.IS_LOGGED_KEY, false)
    }

    private fun stayLogged() {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(Constants.IS_LOGGED_KEY, true)
            apply()
        }
    }

    private fun correctFields(): Boolean {
        if (binding.emailEt.text.isNullOrEmpty()) {
            binding.emailEt.error = "Required field"
            return false
        }
        if (binding.passwordEt.text.isNullOrEmpty()) {
            binding.passwordEt.error = "Required field"
            return false
        }
        return true
    }
}