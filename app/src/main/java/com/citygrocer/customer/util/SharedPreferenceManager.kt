package com.citygrocer.customer.util

import android.content.Context
import android.content.SharedPreferences
import com.citygrocer.customer.MyApplication
import com.citygrocer.customer.constants.Constants.Companion.PREF_CART_DATA
import com.citygrocer.customer.module.CategorySubCategoryResIP
import com.citygrocer.customer.module.Summary
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferenceManager {

    enum class VALUE_TYPE {
        BOOLEAN, INTEGER, STRING, FLOAT, LONG
    }

    companion object {
        val PREFERENCE_NAME = "app_pref"
        val IS_LOGED_IN = "IS_LOGED_IN"
        val NOTIFICATION_STATUS = "NOTIFICATION_STATUS"
        val USER_ID = "USER_ID"
        val MOBILE_NO = "MOBILE_NO"
        val EMAIL_ID = "EMAIL_ID"
        val PASSWORD = "PASSWORD"
        val NAME = "NAME"
        val LASTNAME = "LASTNAME"
        val OTP = "OTP"
        val SOCIAL_ID = "SOCIAL_ID"
        val ID = "ID"
        val LATITUDE = "LATITUDE"
        val LONGITUDE = "LONGITUDE"
        val LOCATION = "LOCATION"
        val SELECTED_AREA = "SELECTED_AREA"
        val MAIN_CAT_LIST_DATA = "MAIN_CAT_LIST_DATA"
        val OTP_VERIFICATION_KEY = "OTP_VERIFICATION_KEY"
        val COUNT = "COUNT"
        val SESSION_ID = "SESSION_ID"
        val DELIVERY_ADDRESS = "DELIVERY_ADDRESS"
        val PREF_LOC = "PREF_LOC"
        val MY_ADDRESS = "MY_ADDRESS"


        fun clearPreferences() {
            getPrefs().edit().clear().apply()
        }

        private fun getPrefs(): SharedPreferences {
            return MyApplication.myApplication.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        }

        fun setPrefVal(key: String, value: Any, vType: VALUE_TYPE) {
            when (vType) {
                VALUE_TYPE.BOOLEAN -> getPrefs().edit().putBoolean(key, value as Boolean).apply()
                VALUE_TYPE.INTEGER -> getPrefs().edit().putInt(key, value as Int).apply()
                VALUE_TYPE.STRING -> getPrefs().edit().putString(key, value as String).apply()
                VALUE_TYPE.FLOAT -> getPrefs().edit().putFloat(key, value as Float).apply()
                VALUE_TYPE.LONG -> getPrefs().edit().putLong(key, value as Long).apply()
                else -> {
                }
            }
        }

        fun getPrefVal(key: String, defValue: Any, vType: VALUE_TYPE): Any? {
            val `object`: Any?
            when (vType) {
                VALUE_TYPE.BOOLEAN -> `object` = getPrefs().getBoolean(key, defValue as Boolean)
                VALUE_TYPE.INTEGER -> `object` = getPrefs().getInt(key, defValue as Int)
                VALUE_TYPE.STRING -> `object` = getPrefs().getString(key, defValue as String)
                VALUE_TYPE.FLOAT -> `object` = getPrefs().getFloat(key, defValue as Float)
                VALUE_TYPE.LONG -> `object` = getPrefs().getLong(key, defValue as Long)
            }
            return `object`
        }

        fun get(key: String, defaultValue: String = ""): String? {
            return getPrefs().getString(key, defaultValue)
        }

        fun isLogIn(): Boolean {
            return getPrefVal(IS_LOGED_IN, false, VALUE_TYPE.BOOLEAN) as Boolean
        }

        fun notificationIsActive(): Boolean {
            return getPrefVal(NOTIFICATION_STATUS, true, VALUE_TYPE.BOOLEAN) as Boolean
        }

        fun getLongitude(): String {
            return getPrefVal(LONGITUDE, "", VALUE_TYPE.STRING) as String
        }

        fun getLatitude(): String {
            return getPrefVal(LATITUDE, "", VALUE_TYPE.STRING) as String
        }

        fun getLocation(): String {
            return getPrefVal(LOCATION, "", VALUE_TYPE.STRING) as String
        }
    }
}