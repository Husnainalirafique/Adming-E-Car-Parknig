package com.saleem.admingecarparking.data

data class ModelUser(
    val email: String,
    val password: String,
){
    constructor():this("","")
}