package com.biggestAsk.ui.activity

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
import com.biggestAsk.ui.main.viewmodel.* // ktlint-disable no-wildcard-imports
import com.biggestAsk.ui.ui.theme.BasicStructureTheme
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * first activity of app all the view models defined here once and used everywhere in app
 */
class HomeActivity : BaseActivity() {
    private var launcher: ManagedActivityResultLauncher<String, Uri?>? = null
    private val mainViewModel: MainViewModel by viewModels()
    private val introViewModel: IntroViewModel by viewModels()
    private val bottomHomeViewModel: BottomHomeViewModel by viewModels()
    private val bottomMilestoneViewModel: BottomMilestoneViewModel by viewModels()
    private val editMilestoneViewModel: EditMilestoneViewModel by viewModels()
    private val yourAccountViewModel: YourAccountViewModel by viewModels()
    private val contactYourProviderViewModel: ContactYourProviderViewModel by viewModels()
    private val communityViewModel: CommunityViewModel by viewModels()
    private val surrogateViewModel: YourSurrogateViewModel by viewModels()
    private val notificationViewModel: NotificationViewModel by viewModels()
    private val aboutAppViewModel: AboutAppViewModel by viewModels()
    private val yourSurrogateMotherViewModel: YourSurrogateMotherViewModel by viewModels()
    private val intendedParentsViewModel: IntendedParentsViewModel by viewModels()
    private val questionViewModel: BottomQuestionViewModel by viewModels()
    private val frequencyViewModel: FrequencyViewModel by viewModels()
    private val privacyPolicyViewModel: PrivacyPolicyViewModel by viewModels()
    private val termsOfServiceViewModel: TermsOfServiceViewModel by viewModels()
    private val detailedSettingsViewModel: DetailedSettingsViewModel by viewModels()
    private val settingViewModel: SettingViewModel by viewModels()
    private val logoutViewModel: LogoutViewModel by viewModels()

    private val permissionReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { it ->
            when {
                it -> {
                    launcher?.launch(Constants.IMAGE_LAUNCHER)
                    communityViewModel.isPermissionAllowed = false
                    yourAccountViewModel.isPermissionAllowed = false
                    contactYourProviderViewModel.isPermissionAllowed = false
                    editMilestoneViewModel.isPermissionAllowed.value = false
                }
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) -> {
                    communityViewModel.isPermissionAllowed = false
                    yourAccountViewModel.isPermissionAllowed = false
                    contactYourProviderViewModel.isPermissionAllowed = false
                    editMilestoneViewModel.isPermissionAllowed.value = false
                }
                else -> {
                    communityViewModel.isPermissionAllowed = true
                    yourAccountViewModel.isPermissionAllowed = true
                    contactYourProviderViewModel.isPermissionAllowed = true
                    editMilestoneViewModel.isPermissionAllowed.value = true
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
                val type = PreferenceProvider(this).getValue(Constants.TYPE, "")
                val updatedImage = PreferenceProvider(this).getValue(Constants.UPDATED_IMAGE, "")
                if (updatedImage != null) {
                    if (type == Constants.PARENT) {
                        yourAccountViewModel.parentImg1 = updatedImage
                    } else {
                        yourAccountViewModel.surrogateImg = updatedImage
                    }
                }
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
                    surrogateViewModel = surrogateViewModel,
                    notificationViewModel = notificationViewModel,
                    aboutAppViewModel = aboutAppViewModel,
                    yourSurrogateMotherViewModel = yourSurrogateMotherViewModel,
                    intendedParentsViewModel = intendedParentsViewModel,
                    questionViewModel = questionViewModel,
                    frequencyViewModel = frequencyViewModel,
                    privacyPolicyViewModel = privacyPolicyViewModel,
                    termsOfServiceViewModel = termsOfServiceViewModel,
                    detailedSettingsViewModel = detailedSettingsViewModel,
                    settingViewModel = settingViewModel,
                    logoutViewModel = logoutViewModel,
                    introViewModel = introViewModel
                )
            }
        }
    }
}
