package com.example.language_app.databases

import android.content.Context.MODE_PRIVATE

class Base(languageApplication: Initialization) {
    private val sharedPref = languageApplication.getSharedPreferences("MyPrefs", MODE_PRIVATE)
    fun saveInt(key: String, value: Int) = with(sharedPref.edit()) {
        putInt(key, value)
        apply()
    }
    fun saveString(key: String, value: String) = with(sharedPref.edit()) {
        putString(key, value)
        apply()
    }
    fun getInt(key: String): Int = sharedPref.getInt(key, 0)

    fun getString(key: String): String = sharedPref.getString(key, null) ?: ""
}