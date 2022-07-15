package com.biggestAsk.util

import android.text.TextUtils
import java.util.regex.Pattern


/**
 * Checks if the provided [String] matches a [Pattern] created with a provided regEx string.
 * @param string the provided string to be checked.
 * @param regex the provided pattern.
 * @return True if string matches the pattern, false otherwise.
 */
fun doesStringMatchPattern(string: String, regex: String): Boolean {
    if (!areStringsValid(string, regex)) return false
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(string)
    return matcher.matches()
}

/**
 * More easily accessible method to get whether AT LEAST ONE String within a provided set is null
 * or 0-length.
 * @param arr the provided String set.
 * @return Whether the provided String set is null or 0-length.
 */
fun areStringsValid(vararg arr: String?): Boolean {
    for (s in arr) {
        if (TextUtils.isEmpty(s)) return false
    }
    return true
}

//return last number from string
fun getLastNumberFormString(phoneNumber: String, digit: Int): String {
    return when {
        phoneNumber.length <= digit -> {
            phoneNumber
        }
        else -> {
            phoneNumber.substring(phoneNumber.length - digit)
        }
    }
}



