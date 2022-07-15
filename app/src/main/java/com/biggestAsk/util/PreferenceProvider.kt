package com.biggestAsk.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class PreferenceProvider(private val appContext: Context) {

    private val mSharedPreferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    /**
     * Set a value for the key
     */
    fun setValue(key: String, value: String) = mSharedPreferences.edit()?.apply {
        putString(key, value)
        apply()
    }


    /**
     * Set a value for the key
     */
    fun setValue(key: String, value: Int) = mSharedPreferences.edit()?.apply {
        putInt(key, value)
        apply()
    }

    /**
     * Set a value for the key
     */
    fun setValue(key: String, value: Double) {
        setValue(key, value.toString())
    }

    /**
     * Set a value for the key
     */
    fun setValue(key: String, value: Long) = mSharedPreferences.edit()?.apply {
        putLong(key, value)
        apply()
    }

    fun setValue(key: String, value: Set<String>) = mSharedPreferences.edit()?.apply {
        putStringSet(key, value)
        apply()
    }

    fun setValue(key: String, value: Boolean) = mSharedPreferences.edit()?.apply {
        putBoolean(key, value)
        apply()
    }

    /**
     * Gets the value from the settings stored natively on the device.
     *
     * @param defaultValue Default value for the key, if one is not found.
     */
    fun getValue(key: String, defaultValue: String): String? =
        mSharedPreferences.getString(key, defaultValue)

    fun getIntValue(key: String, defaultValue: Int): Int =
        mSharedPreferences.getInt(key, defaultValue)

    fun getLongValue(key: String, defaultValue: Long): Long {
        return mSharedPreferences.getLong(key, defaultValue)
    }

    fun getBooleanValue(key: String, defaultValue: Boolean): Boolean {
        return mSharedPreferences.getBoolean(key, defaultValue)
    }

    fun getFloatValue(key: String, defaultValue: Float): Float {
        return mSharedPreferences.getFloat(key, defaultValue)
    }

    fun getStringSet(key: String, defaultValue: Set<String>): Set<String>? {
        return mSharedPreferences.getStringSet(key, defaultValue)
    }

    /**
     * Gets the value from the preferences stored natively on the device.
     *
     * @param defValue Default value for the key, if one is not found.
     */
    fun getValue(key: String, defValue: Boolean): Boolean {
        return mSharedPreferences.getBoolean(key, defValue)
    }

    /**
     * Clear all the preferences store in this [SharedPreferences.Editor]
     */
    fun clear() {
        mSharedPreferences.edit().clear().apply()
    }

    /**
     * Removes preference entry for the given key.
     *
     * @param key Value for the key
     */
    fun removeValue(key: String) {
        mSharedPreferences.edit().remove(key).apply()
    }
}
