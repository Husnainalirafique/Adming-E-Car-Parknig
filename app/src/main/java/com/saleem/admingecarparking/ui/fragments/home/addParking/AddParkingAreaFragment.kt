package com.saleem.admingecarparking.ui.fragments.home.addParking

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.dhaval2404.imagepicker.ImagePicker
import com.saleem.admingecarparking.data.ModelParkingSpace
import com.saleem.admingecarparking.databinding.FragmentAddParkingAreaBinding
import com.saleem.admingecarparking.ui.fragments.home.VmHome
import com.saleem.admingecarparking.ui.fragments.map.MapsActivity
import com.saleem.admingecarparking.utils.Constants
import com.saleem.admingecarparking.utils.DataState
import com.saleem.admingecarparking.utils.Glide
import com.saleem.admingecarparking.utils.ProgressDialogUtil.dismissProgressDialog
import com.saleem.admingecarparking.utils.ProgressDialogUtil.showProgressDialog
import com.saleem.admingecarparking.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddParkingAreaFragment : Fragment() {
    private var _binding: FragmentAddParkingAreaBinding? = null
    private val binding get() = _binding!!
    private lateinit var location: String
    private lateinit var imageUri: Uri
    private val vmHome: VmHome by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddParkingAreaBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
        setUpObserver()
        receiveParcel()
    }

    private fun receiveParcel() {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(Constants.KEY_PARCEL_MODEL_PARKING,ModelParkingSpace::class.java)
        } else {
            arguments?.getParcelable(Constants.KEY_PARCEL_MODEL_PARKING)
        }
        if (data != null){
            binding.btnAddParking.text = "Update Parking"
            com.bumptech.glide.Glide.with(this)
                .load(data.spaceImage)
                .into(binding.imgParkingArea)

            binding.etParkingName.setText(data.spaceName)
            binding.etLocationName.setText(data.spaceLocationInText)
            binding.etPricePerHour.setText(data.pricePerHour)
            binding.etNumSeats.setText(data.numberOfSpots.toString())
            binding.etGoogleMapLocation.setText(data.spaceMapLocation)

            binding.btnAddParking.setOnClickListener {
                if (isValid()) {
                    if (::imageUri.isInitialized){
                        updateParking(data.docId,imageUri.toString())
                    }
                    else{
                        updateParking(data.docId,data.spaceImage)
                    }
                }
            }

        }
    }

    private fun updateParking(docId: String,imageLink: String) {
        val parkingName = binding.etParkingName.text.toString().trim()
        val locationName = binding.etLocationName.text.toString().trim()
        val pricePerHour = binding.etPricePerHour.text.toString().trim()
        val numSpots = binding.etNumSeats.text.toString().toInt()
        val googleMapLocation = binding.etGoogleMapLocation.text.toString().trim()

        val parkingSpace = ModelParkingSpace(parkingName, locationName, pricePerHour, numSpots, googleMapLocation, imageLink,docId) // Set the image URL after image is uploaded
        lifecycleScope.launch {
            vmHome.updateDataModel(parkingSpace)
        }
    }

    private fun setOnClickListener() {
        binding.btnAddParking.setOnClickListener {
            if (isValid()) {
                if (::imageUri.isInitialized){
                    saveParking()
                }
                else{
                    toast("Please select an image!")
                }
            }
        }

        binding.etGoogleMapLocation.setOnClickListener {
            val intent = Intent(requireContext(), MapsActivity::class.java)
            startMapActivityForResult.launch(intent)
        }

        binding.imgParkingArea.setOnClickListener {
            takeImage()
        }
    }

    private fun saveParking() {
        val parkingName = binding.etParkingName.text.toString().trim()
        val locationName = binding.etLocationName.text.toString().trim()
        val pricePerHour = binding.etPricePerHour.text.toString().trim()
        val numSpots = binding.etNumSeats.text.toString().toInt()
        val googleMapLocation = binding.etGoogleMapLocation.text.toString().trim()

        val parkingSpace = ModelParkingSpace(parkingName, locationName, pricePerHour, numSpots, googleMapLocation, imageUri.toString(),"") // Set the image URL after image is uploaded
        lifecycleScope.launch {
            vmHome.uploadImage(parkingSpace)
        }
    }

    private fun setUpObserver(){
        vmHome.parkingAddStatus.observe(viewLifecycleOwner){
            when(it){
                is DataState.Success ->{
                    toast("Done")
                    dismissProgressDialog()
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

    private fun takeImage() {
        ImagePicker.with(this)
            .crop()
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }
    //To get the selected image
    private val startForProfileImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultCode = result.resultCode
        val data = result.data
        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data
                if (fileUri != null) {
                    binding.imgParkingArea.setImageURI(fileUri)
                    imageUri = fileUri
                }
            }

            ImagePicker.RESULT_ERROR -> {
                toast(ImagePicker.getError(data))
            }
        }
    }

    //to get the location latLng
    private val startMapActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    val data: Intent? = result.data
                    val latLngString = data?.getStringExtra("LOCATION")
                    if (latLngString != null) {
                        location = latLngString
                    }
                    binding.etGoogleMapLocation.setText(location)
                }
            }
        }

    private fun isValid(): Boolean {
        binding.etParkingName.error = null
        binding.etLocationName.error = null
        binding.etNumSeats.error = null
        binding.etPricePerHour.error = null
        binding.etGoogleMapLocation.error = null
        return when {
            binding.etParkingName.text.isNullOrEmpty() -> {
                binding.etParkingName.error = "Please fill!"
                binding.etParkingName.requestFocus()
                false
            }

            binding.etLocationName.text.isNullOrEmpty() -> {
                binding.etLocationName.error = "Please fill!"
                binding.etLocationName.requestFocus()
                false
            }

            binding.etNumSeats.text.isNullOrEmpty() -> {
                binding.etNumSeats.error = "Please fill!"
                binding.etNumSeats.requestFocus()
                false
            }

            binding.etPricePerHour.text.isNullOrEmpty() -> {
                binding.etPricePerHour.error = "Please fill!"
                binding.etPricePerHour.requestFocus()
                false
            }

            binding.etGoogleMapLocation.text.isNullOrEmpty() -> {
                binding.etGoogleMapLocation.error = "Please fill!"
                binding.etGoogleMapLocation.requestFocus()
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