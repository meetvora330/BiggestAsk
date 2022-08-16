package com.biggestAsk.ui

import android.Manifest
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.biggestAsk.ui.base.BaseActivity
import com.biggestAsk.ui.homeScreen.HomeScreen
import com.biggestAsk.ui.introScreen.LockScreenOrientation
import com.biggestAsk.ui.main.viewmodel.*
import com.biggestAsk.ui.ui.theme.BasicStructureTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class HomeActivity : BaseActivity() {
    private var launcher: ManagedActivityResultLauncher<String, Uri?>? = null
    private val mainViewModel: MainViewModel by viewModels()
    private val bottomHomeViewModel: BottomHomeViewModel by viewModels()
    private val bottomMilestoneViewModel: BottomMilestoneViewModel by viewModels()
    private val editMilestoneViewModel: EditMilestoneViewModel by viewModels()
    private val yourAccountViewModel: YourAccountViewModel by viewModels()
    private val contactYourProviderViewModel: ContactYourProviderViewModel by viewModels()
    private val communityViewModel: CommunityViewModel by viewModels()
    private val surrogateViewModel: YourSurrogateViewModel by viewModels()

    private val permissionReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { it ->
            when {
                it -> {
                    launcher?.launch("image/*")
                    communityViewModel.isPermissionAllowed = false
                    contactYourProviderViewModel.isPermissionAllowed = false
                }
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) -> {
                    communityViewModel.isPermissionAllowed = false
                    contactYourProviderViewModel.isPermissionAllowed = false
                }
                else -> {
                    communityViewModel.isPermissionAllowed = true
                    contactYourProviderViewModel.isPermissionAllowed = true
                }
            }
        }

    fun callPermissionRequestLauncher(launcher: ManagedActivityResultLauncher<String, Uri?>) {
        this.launcher = launcher
        permissionReqLauncher.launch(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }


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
                HomeScreen(
                    navController = navController,
                    context = this,
                    homeActivity = this,
                    mainViewModel = mainViewModel,
                    bottomHomeViewModel = bottomHomeViewModel,
                    bottomMilestoneViewModel = bottomMilestoneViewModel,
                    editMilestoneViewModel = editMilestoneViewModel,
                    yourAccountViewModel = yourAccountViewModel,
                    contactYourProviderViewModel = contactYourProviderViewModel,
                    communityViewModel = communityViewModel,
                    surrogateViewModel = surrogateViewModel
                )
            }
        }
    }
}