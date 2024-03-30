package com.saleem.admingecarparking.ui.activities.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.saleem.admingecarparking.R
import com.saleem.admingecarparking.databinding.ActivityAuthBinding
import com.saleem.admingecarparking.ui.activities.home.MainActivity
import com.saleem.admingecarparking.utils.startActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    @Inject lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inIt()
    }

    private fun inIt() {
        setOnClickListener()
        checkIfUserLogedIn()
    }

    private fun checkIfUserLogedIn() {
        if (auth.currentUser != null){
            startActivity(MainActivity::class.java)
        }
    }

    private fun setOnClickListener() {

    }
}