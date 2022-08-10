package com.example.recipeapp.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentRegistrationBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var firebaseAuth: FirebaseAuth

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

        firebaseAuth = FirebaseAuth.getInstance()

        binding.createBtn.setOnClickListener {
            if (checkFields()) {
                binding.progressBar.visibility = View.VISIBLE
                firebaseAuth.createUserWithEmailAndPassword(
                    binding.emailEt.text.toString(),
                    binding.passwordEt.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        binding.progressBar.visibility = View.INVISIBLE
                        findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
                    } else {
                        Toast.makeText(context, it.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.signInTxt.setOnClickListener {
            findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
        }
    }


    private fun checkFields(): Boolean {
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