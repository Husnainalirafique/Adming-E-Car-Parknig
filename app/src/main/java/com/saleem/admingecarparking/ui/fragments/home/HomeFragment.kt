package com.saleem.admingecarparking.ui.fragments.home

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.saleem.admingecarparking.R
import com.saleem.admingecarparking.databinding.FragmentHomeBinding
import com.saleem.admingecarparking.ui.activities.auth.AuthActivity
import com.saleem.admingecarparking.utils.BackPressedUtils.goBackPressed
import com.saleem.admingecarparking.utils.startActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var auth: FirebaseAuth
    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private val requestCode = 100

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
        backPressed()
        requestPermission()
    }

    private fun requestPermission() {
        requireActivity().requestPermissions(permissions, requestCode)
    }

    private fun setOnClickListener() {
        binding.tvLogout.setOnClickListener {
            logOut()
        }
        binding.btnAddParkingArea.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addParkingAreaFragment)
        }
    }

    private fun logOut() {
        auth.signOut()
        startActivity(AuthActivity::class.java)
        requireActivity().finish()
    }

    private fun backPressed(){
        goBackPressed {
            requireActivity().finishAffinity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}