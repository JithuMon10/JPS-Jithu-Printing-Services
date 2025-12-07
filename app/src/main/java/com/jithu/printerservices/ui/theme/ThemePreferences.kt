package com.jithu.printerservices.ui.theme

import android.content.Context
import android.content.SharedPreferences

class ThemePreferences(context: Context) {
    private val sharedPrefs: SharedPreferences = 
        context.getSharedPreferences("theme_preferences", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_THEME_MODE = "theme_mode"
        const val THEME_MODE_LIGHT = "light"
        const val THEME_MODE_DARK = "dark"
        const val THEME_MODE_SYSTEM = "system"
    }
    
    fun setThemeMode(mode: String) {
        sharedPrefs.edit().putString(KEY_THEME_MODE, mode).apply()
    }
    
    fun getThemeMode(): String {
        return sharedPrefs.getString(KEY_THEME_MODE, THEME_MODE_SYSTEM) ?: THEME_MODE_SYSTEM
    }
    
    fun isDarkMode(systemInDarkTheme: Boolean): Boolean {
        return when (getThemeMode()) {
            THEME_MODE_LIGHT -> false
            THEME_MODE_DARK -> true
            else -> systemInDarkTheme
        }
    }
}
