package com.example.learnjetpackcompose.Utils

import android.content.Context
import android.content.res.Configuration
import java.util.*

fun updateLocale(context: Context, language: String): Context {
    val locale = when (language) {
        "English" -> Locale("en")
        "Korean" -> Locale("ko")
        "French" -> Locale("fr")
        "Vietnamese" -> Locale("vi")
        "Chinese" -> Locale("zh")
        else -> Locale.getDefault()
    }

    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    return context.createConfigurationContext(config)
}