package com.biggestAsk.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import com.biggestAsk.util.PreferenceProvider
import com.biggestAsk.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
open class BaseActivity : ComponentActivity() {

    @Inject
    lateinit var mPreferenceProvider: PreferenceProvider

    override fun onDestroy() {
        hideKeyboard()
        super.onDestroy()
    }
}