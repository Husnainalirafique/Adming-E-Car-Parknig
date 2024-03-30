package com.saleem.admingecarparking.ui.activities.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saleem.admingecarparking.R
import com.saleem.admingecarparking.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inIt()
    }

    private fun inIt() {
        setOnClickListener()
    }

    private fun setOnClickListener() {

    }
}