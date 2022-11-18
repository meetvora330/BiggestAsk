package com.biggestAsk.util

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * File holding all the methods of general interest.
 * Create by Bharat.
 */

//hide the keyboard
fun Activity.HideKeyboard() {
    val imm: InputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = currentFocus
    if (view == null) view = View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}