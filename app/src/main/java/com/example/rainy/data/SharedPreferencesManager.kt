package com.example.rainy.data

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferencesManager @Inject constructor
    (private val sharedPreferences: SharedPreferences){

    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}