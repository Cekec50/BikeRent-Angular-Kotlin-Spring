package com.example.bikerentandroid

import android.content.Context
import android.content.SharedPreferences
import com.example.bikerentandroid.model.User

object SessionManager {

    private const val PREFS_NAME = "bikerent_session"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USERNAME = "username"
    private const val KEY_FIRST_NAME = "first_name"
    private const val KEY_LAST_NAME = "last_name"
    private const val KEY_EMAIL = "email"
    private const val KEY_PHONE = "phone"
    private const val KEY_IS_ADMIN = "is_admin"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveUser(context: Context, user: User) {
        prefs(context).edit()
            .putLong(KEY_USER_ID, user.id)
            .putString(KEY_USERNAME, user.username)
            .putString(KEY_FIRST_NAME, user.firstName)
            .putString(KEY_LAST_NAME, user.lastName)
            .putString(KEY_EMAIL, user.email)
            .putString(KEY_PHONE, user.phone)
            .putBoolean(KEY_IS_ADMIN, user.isAdmin)
            .apply()
    }

    fun getCurrentUser(context: Context): User? {
        val p = prefs(context)
        val id = p.getLong(KEY_USER_ID, -1L)
        if (id < 0) return null
        return User(
            id = id,
            username = p.getString(KEY_USERNAME, null),
            firstName = p.getString(KEY_FIRST_NAME, null),
            lastName = p.getString(KEY_LAST_NAME, null),
            phone = p.getString(KEY_PHONE, null),
            email = p.getString(KEY_EMAIL, null),
            isAdmin = p.getBoolean(KEY_IS_ADMIN, false)
        )
    }

    fun getUserId(context: Context): Long = prefs(context).getLong(KEY_USER_ID, -1L)

    fun isLoggedIn(context: Context): Boolean = getUserId(context) >= 0

    fun clear(context: Context) {
        prefs(context).edit().clear().apply()
    }
}
