package com.saleem.admingecarparking.preferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.saleem.admingecarparking.data.ModelUser
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceManager @Inject constructor(@ApplicationContext private val context: Context) {
    companion object{
        private const val PREF_NAME = "MyPrefs"
        private const val KEY_STUDENT = "MyPrefs"
    }

    private val gson = Gson()

    private val myPref:SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
    }

    fun saveUserData(student: ModelUser) {
        val json = gson.toJson(student)
        myPref.edit().putString(KEY_STUDENT,json).apply()
    }

    fun getUserData(): ModelUser?{
        val json = myPref.getString(KEY_STUDENT, null)
        return gson.fromJson(json, ModelUser::class.java)
    }
}