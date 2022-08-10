package com.example.recipeapp.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentLoginBinding
import com.example.recipeapp.utils.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

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

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signUpTxt.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
        }

        binding.loginBtn.setOnClickListener {
            if (checkFields()) {
                binding.progressBar.visibility = View.VISIBLE
                firebaseAuth.signInWithEmailAndPassword(
                    binding.emailEt.text.toString(),
                    binding.passwordEt.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        stayLogged()
                        binding.progressBar.visibility = View.INVISIBLE
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    } else {
                        Toast.makeText(context, it.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun checkIfLogged(): Boolean {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean(Utils.IS_LOGGED_KEY, false)
    }

    private fun stayLogged() {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(Utils.IS_LOGGED_KEY, true)
            apply()
        }
    }

    private fun checkFields(): Boolean {
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