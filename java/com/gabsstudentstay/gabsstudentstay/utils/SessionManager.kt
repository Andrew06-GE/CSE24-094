package com.gabsstudentstay.gabsstudentstay.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME        = "GabsStudentStaySession"
        private const val KEY_USER_ID      = "userId"
        private const val KEY_USER_NAME    = "userName"
        private const val KEY_USER_EMAIL   = "userEmail"
        private const val KEY_USER_ROLE    = "userRole"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_IS_SEEDED    = "isSeeded"
    }

    fun saveSession(userId: Int, name: String, email: String, role: String) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_ROLE, role)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun clearSession() {
        prefs.edit().apply {
            remove(KEY_USER_ID)
            remove(KEY_USER_NAME)
            remove(KEY_USER_EMAIL)
            remove(KEY_USER_ROLE)
            putBoolean(KEY_IS_LOGGED_IN, false)
            apply()
        }
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun getUserId(): Int    = prefs.getInt(KEY_USER_ID, -1)
    fun getUserName(): String  = prefs.getString(KEY_USER_NAME, "") ?: ""
    fun getUserEmail(): String = prefs.getString(KEY_USER_EMAIL, "") ?: ""
    fun getUserRole(): String  = prefs.getString(KEY_USER_ROLE, "student") ?: "student"

    // Tracks whether the DB has been seeded with dummy data on first launch
    fun isSeeded(): Boolean = prefs.getBoolean(KEY_IS_SEEDED, false)
    fun markSeeded() = prefs.edit().putBoolean(KEY_IS_SEEDED, true).apply()
}