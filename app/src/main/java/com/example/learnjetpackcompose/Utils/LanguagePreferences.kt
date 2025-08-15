package com.example.learnjetpackcompose.Utils

import android.content.Context
import android.content.SharedPreferences

class LanguagePreferences(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LANGUAGE = "language"
        private const val DEFAULT_LANGUAGE = "English"
    }

    fun saveLanguage(language: String) {
        preferences.edit().putString(KEY_LANGUAGE, language).apply()
    }

    fun getLanguage(): String {
        return preferences.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }
}