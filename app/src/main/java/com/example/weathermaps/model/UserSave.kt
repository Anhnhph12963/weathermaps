package com.example.weathermaps.model

import android.content.SharedPreferences

data class UserSave(
    var username: String,
    var imageLink: String,
    private val sharedPref: SharedPreferences
) {
    private val KEY_USERNAME = "username"
    private val KEY_USERLINK = "imagelink"

    fun Save() {
        with(sharedPref.edit()) {
            putString(KEY_USERNAME, username)
            putString(KEY_USERLINK, imageLink)
            commit()
        }
    }

    fun loadName(): String = sharedPref.getString(KEY_USERNAME, "Default") ?: "Default"
    fun loadImage(): String = sharedPref.getString(KEY_USERLINK, "Default") ?: "Default"
}
