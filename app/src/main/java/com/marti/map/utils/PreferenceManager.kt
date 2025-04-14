package com.marti.map.utils

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceManager @Inject constructor(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFERENCE_NAME,
        Context.MODE_PRIVATE
    )

    fun saveDarkModeState(isDarkMode: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_MODE, isDarkMode).apply()
    }

    fun getDarkModeState(): Boolean {
        return prefs.getBoolean(KEY_DARK_MODE, false)
    }
    companion object {
        private const val PREFERENCE_NAME = "MartiMapPrefs"
        private const val KEY_DARK_MODE = "isDarkMode"
    }
} 