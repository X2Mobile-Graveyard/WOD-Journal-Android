package com.x2mobile.wodjar.business

import android.content.Context
import android.preference.PreferenceManager
import android.text.TextUtils
import com.x2mobile.wodjar.data.model.UnitType

object Preference {

    private val TOKEN = "token"
    private val USER_ID = "userId"
    private val EMAIL = "email"
    private val DISPLAY_NAME = "displayName"
    private val PROFILE_PICTURE_URL = "profilePictureUrl"
    private val UNIT_TYPE = "unitType"

    fun getToken(context: Context): String? = PreferenceManager.getDefaultSharedPreferences(context).getString(TOKEN, null)

    fun setToken(context: Context, token: String?) = PreferenceManager.getDefaultSharedPreferences(context).edit().putString(TOKEN, token).apply()

    fun getUserId(context: Context): Int = PreferenceManager.getDefaultSharedPreferences(context).getInt(USER_ID, Constants.ID_NA)

    fun setUserId(context: Context, userId: Int) = PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(USER_ID, userId).apply()

    fun isLoggedIn(context: Context): Boolean = !TextUtils.isEmpty(getToken(context)) && getUserId(context) > Constants.ID_NA

    fun getEmail(context: Context): String = PreferenceManager.getDefaultSharedPreferences(context).getString(EMAIL, null)

    fun setEmail(context: Context, email: String) = PreferenceManager.getDefaultSharedPreferences(context).edit().putString(EMAIL, email).apply()

    fun getDisplayName(context: Context): String? = PreferenceManager.getDefaultSharedPreferences(context).getString(DISPLAY_NAME, null)

    fun setDisplayName(context: Context, displayName: String?) = PreferenceManager.getDefaultSharedPreferences(context).edit().putString(DISPLAY_NAME, displayName).apply()

    fun getProfilePictureUrl(context: Context): String? = PreferenceManager.getDefaultSharedPreferences(context).getString(PROFILE_PICTURE_URL, null)

    fun setProfilePictureUrl(context: Context, url: String?) = PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PROFILE_PICTURE_URL, url).apply()

    fun getUnitType(context: Context): UnitType = UnitType.values()[Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString(UNIT_TYPE, "0"))]

    fun clear(context: Context) = PreferenceManager.getDefaultSharedPreferences(context).edit()
            .remove(TOKEN).remove(USER_ID).remove(EMAIL).remove(DISPLAY_NAME).remove(PROFILE_PICTURE_URL)
            .apply()

}
