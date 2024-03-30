package com.saleem.admingecarparking.ui.fragments.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.saleem.admingecarparking.data.ModelUser
import com.saleem.admingecarparking.preferences.PreferenceManager
import com.saleem.admingecarparking.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VmAuth @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _signUpStatus = MutableLiveData<DataState<Nothing>>()
    val signUpStatus: LiveData<DataState<Nothing>> = _signUpStatus

    private val _loginStatus = MutableLiveData<DataState<Nothing>>()
    val loginStatus: LiveData<DataState<Nothing>> = _loginStatus

    suspend fun signUpWithEmailPass(user: ModelUser) {
        withContext(Dispatchers.Main) { _signUpStatus.value = DataState.Loading }
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnSuccessListener {
                addUserToDb(user)
            }
            .addOnFailureListener { exception ->
                if (exception is FirebaseAuthUserCollisionException) {
                    _signUpStatus.value = DataState.Error("User already exists.")
                } else {
                    _signUpStatus.value = DataState.Error("Authentication failed.")
                }
            }
    }

    private fun addUserToDb(user: ModelUser) {
        db.collection("users").document(auth.currentUser?.uid!!).set(user)
            .addOnSuccessListener {
                preferenceManager.saveUserData(user)
                _signUpStatus.value = DataState.Success()
            }
            .addOnFailureListener {
                _signUpStatus.value = DataState.Error(it.message!!)
            }
    }

    suspend fun loginWithEmailPass(email: String, password: String) {
        withContext(Dispatchers.Main) { _loginStatus.value = DataState.Loading }
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                getUserFromDbAndSave()
            }
            .addOnFailureListener { exception ->
                if (exception is FirebaseAuthInvalidCredentialsException) {
                    _loginStatus.value = DataState.Error("Wrong credentials Or not signed up.")
                } else {
                    _loginStatus.value = DataState.Error("Authentication failed.")
                }
            }
    }

    private fun getUserFromDbAndSave() {
        db.collection("users").document(auth.currentUser?.uid!!)
            .get()
            .addOnSuccessListener {
                val user = it.toObject<ModelUser>()
                if (user != null) {
                    preferenceManager.saveUserData(user)
                    _loginStatus.value = DataState.Success()
                }
            }
            .addOnFailureListener {
                _loginStatus.value = DataState.Error(it.message!!)
            }
    }
}