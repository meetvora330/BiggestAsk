package com.biggestAsk.ui.base

import androidx.activity.ComponentActivity
import com.biggestAsk.util.PreferenceProvider
import com.biggestAsk.util.HideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
open class BaseActivity : ComponentActivity() {

    @Inject
    lateinit var mPreferenceProvider: PreferenceProvider

    override fun onDestroy() {
        HideKeyboard()
        super.onDestroy()
    }
}