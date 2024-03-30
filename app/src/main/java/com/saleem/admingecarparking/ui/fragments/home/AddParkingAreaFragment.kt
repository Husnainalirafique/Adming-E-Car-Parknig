package com.saleem.admingecarparking.ui.fragments.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.saleem.admingecarparking.databinding.FragmentAddParkingAreaBinding
import com.saleem.admingecarparking.ui.fragments.map.MapsActivity
import com.saleem.admingecarparking.utils.toast

class AddParkingAreaFragment : Fragment() {
    private var _binding: FragmentAddParkingAreaBinding? = null
    private val binding get() = _binding!!
    private lateinit var location: String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddParkingAreaBinding.inflate(inflater, container, false)


        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnAddLocation.setOnClickListener {
            val intent = Intent(requireContext(), MapsActivity::class.java)
            startMapActivityForResult.launch(intent)
        }
        binding.btnOpenLocation.setOnClickListener {

        }
    }

    private val startMapActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val latLngString = data?.getStringExtra("LOCATION")
            if (latLngString != null){
                location = latLngString
            }
            toast(location)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}