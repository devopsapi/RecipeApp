package com.example.recipeapp.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentRegistrationBinding
import com.example.recipeapp.ui.util.Routes
import com.example.recipeapp.ui.util.UiEvent
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.GONE

        binding.createBtn.setOnClickListener {
            if (correctFields()) {
                authViewModel.registerUser(
                    binding.emailEt.text.toString(),
                    binding.passwordEt.text.toString()
                )
            }
        }

        binding.signInTxt.setOnClickListener {
            navigateToRoute(Routes.NAVIGATE_TO_LOGIN)
        }

        observeData()
    }

    private fun observeData() {
        authViewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                is UiEvent.Loading -> {
                    binding.createBtn.isEnabled = !uiEvent.isLoading
                    binding.createBtn.background =
                        if (uiEvent.isLoading) resources.getDrawable(R.drawable.disabled_btn) else resources.getDrawable(
                            R.drawable.round_violet_btn
                        )
                    binding.progressBar.visibility =
                        if (uiEvent.isLoading) View.VISIBLE else View.INVISIBLE
                }
                is UiEvent.Navigate -> navigateToRoute(uiEvent.route)
                is UiEvent.ShowToast -> Toast.makeText(context, uiEvent.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun navigateToRoute(route: String) {
        when (route) {
            Routes.NAVIGATE_TO_LOGIN -> findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
            Routes.NAVIGATE_TO_HOME -> findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragmentToHomeFragment())
            else -> findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
        }
    }

    private fun correctFields(): Boolean {
        if (binding.emailEt.text.isNullOrEmpty()) {
            binding.emailEt.error = "Required field"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEt.text.toString()).matches()) {
            binding.emailEt.error = "Invalid email"
            return false
        }
        if (binding.passwordEt.text.isNullOrEmpty()) {
            binding.passwordEt.error = "Required field"
            return false
        }
        if (binding.passwordEt.text?.length!! < 6) {
            binding.passwordEt.error = "Password should contain at least 6 characters"
            return false
        }
        if (binding.confirmPasswordEt.text?.isEmpty() == true) {
            binding.confirmPasswordEt.error = "Required field"
            return false
        }
        val confirmPass = binding.confirmPasswordEt.text.toString().trim()
        val pass = binding.passwordEt.text.toString().trim()
        if ((pass == confirmPass).not()) {
            binding.confirmPasswordEt.error = "Passwords do not match"
            return false
        }

        return true
    }
}