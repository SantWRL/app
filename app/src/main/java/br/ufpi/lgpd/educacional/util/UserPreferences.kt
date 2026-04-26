package br.ufpi.lgpd.educacional.util

import android.content.Context

class UserPreferences(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)

    var userName: String
        get() = preferences.getString(KEY_USER_NAME, DEFAULT_NAME) ?: DEFAULT_NAME
        set(value) {
            preferences.edit()
                .putString(KEY_USER_NAME, value.trim().takeIf { it.isNotBlank() } ?: DEFAULT_NAME)
                .apply()
        }

    fun getInitials(): String {
        val words = userName.trim().split(" ").filter { it.isNotBlank() }
        return when {
            words.isEmpty() -> "U"
            words.size == 1 -> words.first().take(2).uppercase()
            else -> (words.first().first().toString() + words.last().first().toString()).uppercase()
        }
    }

    companion object {
        private const val PREFS_FILE = "lgpd_educacional_prefs"
        private const val KEY_USER_NAME = "user_name"
        private const val DEFAULT_NAME = "Usuário"
    }
}
