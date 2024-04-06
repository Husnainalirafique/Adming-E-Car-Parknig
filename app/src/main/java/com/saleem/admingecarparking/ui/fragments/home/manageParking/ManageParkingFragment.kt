package com.saleem.admingecarparking.ui.fragments.home.manageParking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.saleem.admingecarparking.R
import com.saleem.admingecarparking.databinding.FragmentManageParkingBinding
import com.saleem.admingecarparking.ui.fragments.home.VmHome
import com.saleem.admingecarparking.utils.Constants
import com.saleem.admingecarparking.utils.DataState
import com.saleem.admingecarparking.utils.ProgressDialogUtil.dismissProgressDialog
import com.saleem.admingecarparking.utils.ProgressDialogUtil.showProgressDialog
import com.saleem.admingecarparking.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageParkingFragment : Fragment() {
    private var _binding: FragmentManageParkingBinding? = null
    private val binding get() = _binding!!
    private val vmHome: VmHome by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentManageParkingBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
        setUpObserver()
    }

    private fun setOnClickListener() {

    }

    private fun setUpObserver(){
        vmHome.parkingSpaces.observe(viewLifecycleOwner){
            when(it){
                is DataState.Success ->{
                    val listOfParking = it.data
                    if (listOfParking != null){
                        binding.rvParking.adapter = AdapterParkingSpaces(listOfParking){modelParkingSpace ->
                            val bundle = Bundle()
                            bundle.putParcelable(Constants.KEY_PARCEL_MODEL_PARKING,modelParkingSpace)
                            findNavController().navigate(R.id.action_manageParkingFragment_to_addParkingAreaFragment,bundle)
                        }
                        dismissProgressDialog()
                    }
                }
                is DataState.Error ->{
                    toast(it.errorMessage)
                    dismissProgressDialog()
                }
                is DataState.Loading ->{
                    showProgressDialog()
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}