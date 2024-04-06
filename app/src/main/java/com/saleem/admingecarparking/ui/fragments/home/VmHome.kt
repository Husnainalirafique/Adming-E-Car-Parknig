package com.saleem.admingecarparking.ui.fragments.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.saleem.admingecarparking.data.ModelParkingSpace
import com.saleem.admingecarparking.data.ModelSpot
import com.saleem.admingecarparking.utils.DataState
import com.saleem.admingecarparking.utils.ImageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class VmHome @Inject constructor(
    private val db:FirebaseFirestore,
    private val storageRef: StorageReference,
    private val context: Context
): ViewModel() {

    private val _parkingAddStatus = MutableLiveData<DataState<Nothing>>()
    val parkingAddStatus: LiveData<DataState<Nothing>> = _parkingAddStatus

    private val _parkingSpaces = MutableLiveData<DataState<List<ModelParkingSpace>>>()
    val parkingSpaces: LiveData<DataState<List<ModelParkingSpace>>> get() = _parkingSpaces

    init {
        viewModelScope.launch {
            fetchParkingSpaces()
        }
    }

    fun uploadImage(parkingSpace: ModelParkingSpace) {
        _parkingAddStatus.value = DataState.Loading
        val imageRef = storageRef.child("parking_images/${System.currentTimeMillis()}")

        val bitmap = ImageUtils.uriToBitmap(context, parkingSpace.spaceImage.toUri())
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
        val data = byteArrayOutputStream.toByteArray()

        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                parkingSpace.spaceImage = imageUrl

                saveParkingAreaWithSpots(parkingSpace)
            }.addOnFailureListener { e ->
                _parkingAddStatus.value = e.message?.let { it1 -> DataState.Error(it1) }
            }
        }.addOnFailureListener { e ->
            _parkingAddStatus.value = e.message?.let { it1 -> DataState.Error(it1) }
        }
    }

    private fun generateSpotNames(numSpots: Int): List<String> {
        val listOfSpots = mutableListOf<String>()
        for (i in 1..numSpots){
            listOfSpots.add("A$i")
        }
        return listOfSpots
    }

    private fun saveParkingAreaWithSpots(parkingSpace: ModelParkingSpace){
        db.collection("parkingSpaces")
            .add(parkingSpace)
            .addOnSuccessListener {
                addSpots(parkingSpace,it.id)
                _parkingAddStatus.value = DataState.Success()
            }
            .addOnFailureListener { e ->
                _parkingAddStatus.value = e.message?.let { it1 -> DataState.Error(it1) }
            }
    }

    private fun fetchParkingSpaces() {
        _parkingSpaces.value = DataState.Loading

        db.collection("parkingSpaces")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    _parkingSpaces.value = DataState.Error(firebaseFirestoreException.message ?: "Unknown error")
                    return@addSnapshotListener
                }

                val parkingList = mutableListOf<ModelParkingSpace>()
                for (document in querySnapshot!!) {
                    // Convert Firestore document to ModelParking object
                    val parkingSpace = document.toObject(ModelParkingSpace::class.java)
                    val docId = document.id
                    parkingSpace.docId = docId
                    parkingList.add(parkingSpace)
                }
                _parkingSpaces.value = DataState.Success(parkingList)
            }
    }

    fun updateDataModel(parking: ModelParkingSpace) {
        _parkingAddStatus.value = DataState.Loading
        val db = FirebaseFirestore.getInstance()
        val documentRef = db.collection("parkingSpaces").document(parking.docId)
        documentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    documentRef.set(parking)
                        .addOnSuccessListener {
                            addSpots(parking,parking.docId)
                            _parkingAddStatus.value = DataState.Success()
                        }
                        .addOnFailureListener { e ->
                            _parkingAddStatus.value = DataState.Error(e.message!!)
                        }
                } else {
                    _parkingAddStatus.value = DataState.Error("No doc")
                }
            }
            .addOnFailureListener { e ->
                _parkingAddStatus.value = DataState.Error(e.message!!)
            }
    }

    private fun addSpots(parking: ModelParkingSpace,docId: String){
        val spotNames = generateSpotNames(parking.numberOfSpots)
        val spotsCollectionRef = db.collection("parkingSpaces").document(docId).collection("spots")

        spotNames.forEach { spotName ->
            val spotModel = ModelSpot(spotName,false)
            spotsCollectionRef.document(spotName)
                .set(spotModel)
                .addOnFailureListener { e ->
                    _parkingAddStatus.value = e.message?.let { it1 -> DataState.Error(it1) }
                }
        }
    }

}