package com.saleem.admingecarparking.ui.fragments.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
    private val LOCATION_SETTINGS_REQUEST_CODE = 101
    private val binding get() = _binding!!
    @Inject lateinit var auth: FirebaseAuth
    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private val requestCode = 100

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        checkLocationEnabled()
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
        binding.btnManageSpaces.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_manageParkingFragment)
        }
    }

    private fun logOut() {
        auth.signOut()
        startActivity(AuthActivity::class.java)
        requireActivity().finish()
    }


    private fun checkLocationEnabled() {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showEnableLocationDialog()
        }
    }

    private fun showEnableLocationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Location Services Disabled")
            .setMessage("Please enable location services to use this feature.")
            .setCancelable(false)
            .setPositiveButton("Enable Location") { dialog, _ ->
                navigateToLocationSettings()
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun navigateToLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(intent, LOCATION_SETTINGS_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_SETTINGS_REQUEST_CODE) {
            // Check location status when returning from location settings
            checkLocationEnabled()
        }
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