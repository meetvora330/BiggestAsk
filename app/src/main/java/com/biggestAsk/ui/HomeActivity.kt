package com.biggestAsk.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.biggestAsk.ui.homeScreen.HomeScreen
import com.biggestAsk.ui.introScreen.LockScreenOrientation
import com.biggestAsk.ui.ui.theme.BasicStructureTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = MaterialTheme.colors.isLight
            val navController = rememberNavController()
            SideEffect {
                // Update all of the system bar colors to be transparent, and use
                // dark icons if we're in light theme
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )
            }
            BasicStructureTheme {
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                HomeScreen(navController = navController)
            }
        }
    }
}