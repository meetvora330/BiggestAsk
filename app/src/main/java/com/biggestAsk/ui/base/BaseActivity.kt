package com.biggestAsk.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.biggestAsk.util.PreferenceProvider
import com.biggestAsk.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
open class BaseActivity : ComponentActivity() {

    @Inject
    lateinit var mPreferenceProvider: PreferenceProvider

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onDestroy() {
        hideKeyboard()
        super.onDestroy()
    }
}