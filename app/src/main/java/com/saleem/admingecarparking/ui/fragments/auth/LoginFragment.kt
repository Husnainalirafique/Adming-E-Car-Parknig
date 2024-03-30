package com.saleem.admingecarparking.ui.fragments.auth

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.saleem.admingecarparking.R
import com.saleem.admingecarparking.databinding.FragmentLoginBinding
import com.saleem.admingecarparking.ui.activities.home.MainActivity
import com.saleem.admingecarparking.utils.BackPressedUtils.goBackPressed
import com.saleem.admingecarparking.utils.DataState
import com.saleem.admingecarparking.utils.EditTextUtils
import com.saleem.admingecarparking.utils.ProgressDialogUtil.dismissProgressDialog
import com.saleem.admingecarparking.utils.ProgressDialogUtil.showProgressDialog
import com.saleem.admingecarparking.utils.startActivity
import com.saleem.admingecarparking.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val vm: VmAuth by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
        backPressed()
        setupObserver()
        setUpUi()
    }

    private fun setOnClickListener() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnLogin.setOnClickListener {
            if (isValid()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    vm.loginWithEmailPass(
                        binding.etEmail.text.toString(),
                        binding.etPassword.text.toString()
                    )
                }
            }
        }
        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    private fun setupObserver() {
        vm.loginStatus.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    toast("Loged In successfully")
                    dismissProgressDialog()
                    startActivity(MainActivity::class.java)
                    requireActivity().finish()
                }

                is DataState.Error -> {
                    toast(dataState.errorMessage)
                    dismissProgressDialog()
                }

                is DataState.Loading -> {
                    showProgressDialog()
                }
            }
        }

    }

    private fun setUpUi(){
        EditTextUtils.setPasswordVisibilityToggle(binding.etPassword)
    }

    private fun backPressed() {
        goBackPressed {
            requireActivity().finishAffinity()
        }
    }

    private fun isValid(): Boolean {
        binding.etEmail.error = null
        binding.etPassword.error = null
        return when {
            binding.etEmail.text.isNullOrEmpty() -> {
                binding.etEmail.error = "Please fill!"
                binding.etEmail.requestFocus()
                false
            }

            binding.etPassword.text.isNullOrEmpty() -> {
                binding.etPassword.error = "Please fill!"
                binding.etPassword.requestFocus()
                false
            }

            binding.etPassword.text.toString().length < 7 -> {
                binding.etPassword.error = "Password must be at least 7 characters long"
                binding.etPassword.requestFocus()
                false
            }

            !Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches() -> {
                binding.etEmail.error = "Invalid email pattern"
                binding.etEmail.requestFocus()
                false
            }

            else -> {
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
