package com.biggestAsk.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.os.SystemClock
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.biggestAsk.R

/**
 * File holding all the methods of general interest.
 * Create by Bharat.
 */

/**
 * This method converts dp units to pixels.
 *
 * @param dp        the wanted dp units.
 * @param resources the application's `Resources`
 * @return The corresponding pixel value.
 */
fun dpToPx(dp: Float, resources: Resources): Int {
    return convertToPx(TypedValue.COMPLEX_UNIT_DIP, dp, resources)
}

/**
 * This method converts dp units to pixels.
 *
 * @param dp        the wanted dp units.
 * @param resources the application's `Resources`
 * @return The corresponding pixel value.
 */
fun dpToPxFloat(dp: Float, resources: Resources): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
}

/**
 * This method converts any unit to pixels.
 *
 * @param unit      the wanted unit.
 * @param value     the wanted value units.
 * @param resources the application's `Resources`
 * @return The corresponding pixel value.
 */
private fun convertToPx(unit: Int, value: Float, resources: Resources): Int {
    val px = TypedValue.applyDimension(unit, value, resources.displayMetrics)
    return px.toInt()
}


fun isNotch(resources: Resources): Boolean {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    val statusBarHeight = if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else 0
    return statusBarHeight > dpToPx(24f, resources)
}

/**
 * Add replace fragment
 *
 * @param container
 * @param fragment
 * @param addFragment
 * @param addToBackStack
 */
fun FragmentActivity.addReplaceFragment(
    @IdRes container: Int,
    fragment: Fragment,
    addFragment: Boolean,
    addToBackStack: Boolean,
) {
    val transaction: FragmentTransaction? = supportFragmentManager.beginTransaction()
    if (addFragment) {
        transaction?.add(container, fragment, fragment.javaClass.simpleName)
    } else {
        transaction?.replace(container, fragment, fragment.javaClass.simpleName)
    }
    if (addToBackStack) {
        transaction?.addToBackStack(fragment.tag)
    }
    hideKeyboard()
    transaction?.commit()
}

/**
 * Add replace fragment
 *
 * @param container
 * @param fragment
 * @param addFragment
 * @param addToBackStack
 */
fun FragmentActivity.addReplaceFragmentWithAnimation(
    @IdRes container: Int,
    fragment: Fragment,
    addFragment: Boolean,
    addToBackStack: Boolean,
    enterAnimation: Int,
    exitAnimation: Int,
) {
    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
    transaction.setCustomAnimations(enterAnimation, exitAnimation)
    if (addFragment) {
        transaction.add(container, fragment, fragment.javaClass.simpleName)
    } else {
        transaction.replace(container, fragment, fragment.javaClass.simpleName)
    }
    if (addToBackStack) {
        transaction.addToBackStack(fragment.tag)
    }
    hideKeyboard()
    transaction.commit()
}

/**Get current active Fragment in a container of a activity
 * @param container -> Container holder id  of fragment of a activity
 * **/
fun AppCompatActivity.getCurrentFragment(@IdRes container: Int): Fragment? =
    supportFragmentManager.findFragmentById(container)

fun Activity.hideSystemUI() {
    window.apply {
        addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            setDecorFitsSystemWindows(false)
            val controller = decorView.windowInsetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            statusBarColor = Color.TRANSPARENT
        } else {
            decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}

fun Activity.statusBarDarkIcons(color: Int? = null, isLightStatusBar: Boolean = true) {
    window.apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            setDecorFitsSystemWindows(false)
            val controller = window.insetsController
            if (controller != null) {
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                isLightStatusBar -> {
                    decorView.systemUiVisibility =
                        (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                }
            }
            statusBarColor =
                color ?: ContextCompat.getColor(context, R.color.colorPrimary)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            statusBarColor =
                color ?: ContextCompat.getColor(context, R.color.colorPrimary)
        } else {
            decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}

//hide the keyboard
fun Activity.hideKeyboard() {
    val imm: InputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = currentFocus
    if (view == null) view = View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

//show the keyboard
fun Activity.showKeyboard() {
    val view = currentFocus
    val methodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    methodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun isKeyboardShown(rootView: View): Boolean {
    /* 128dp = 32dp * 4, minimum button height 32dp and generic 4 rows soft keyboard */
    val softKeyboardHeightThreshold = 128
    val r = Rect()
    rootView.getWindowVisibleDisplayFrame(r)
    val dm = rootView.resources.displayMetrics
    /* heightDiff = rootView height - status bar height (r.top) - visible frame height (r.bottom - r.top) */
    val heightDiff = rootView.bottom - r.bottom
    /* Threshold size: dp to pixels, multiply with display density */

    return heightDiff > softKeyboardHeightThreshold * dm.density
}

fun EditText.showKeyboard() {
    post {
        requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun isViewVisible(view: View?): Boolean {
    if (view == null) {
        return false
    }
    if (!view.isShown) {
        return false
    }
    val actualPosition = Rect()
    view.getGlobalVisibleRect(actualPosition)
    val screen =
        Rect(0, 0, getScreenWidth(), getScreenHeight())
    return actualPosition.intersect(screen)
}

fun getScreenHeight(): Int = Resources.getSystem().displayMetrics.heightPixels

fun getScreenWidth(): Int = Resources.getSystem().displayMetrics.widthPixels


/**
 * To check soft navigation is on phone or not
 */
@SuppressLint("ObsoleteSdkInt")
fun Activity.isNavigationBarShow(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        val display = windowManager.defaultDisplay
        val size = Point()
        val realSize = Point()
        display.getSize(size)
        display.getRealSize(realSize)
        realSize.y != size.y
    } else {
        val menu = ViewConfiguration.get(this).hasPermanentMenuKey()
        val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        !(menu || back)
    }
}

/**
 * return Height of navigation bar
 */
fun Activity.getHeightOfNavigationBar(): Int {
    var navigationBarHeight = 0
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceId > 0) {
        navigationBarHeight = resources.getDimensionPixelSize(resourceId)
    }
    return navigationBarHeight
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as
            InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun FragmentActivity.gotoFirstFragment() {
    supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

//change the Orientation
fun isOrientationLandscape(resources: Resources): Boolean {
    val orientation = resources.configuration.orientation
    return orientation == Configuration.ORIENTATION_LANDSCAPE
}

//Check Rotation the Orientation
fun changeOrientation(activity: Activity, shouldLandscape: Boolean) {
    activity.requestedOrientation = if (shouldLandscape) {
        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    } else {
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}

fun View.clickWithDebounce(debounceTime: Long = 1200L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun String?.isEmailValid(): Boolean {
    return !this.isNullOrEmpty() && doesStringMatchPattern(this, Constants.REGEX_EMAIL)
}

fun String?.isPasswordValid(): Boolean =
    !this.isNullOrEmpty() && this.length >= 6
