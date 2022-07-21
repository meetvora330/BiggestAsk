@file:Suppress("OPT_IN_IS_NOT_ENABLED", "KotlinConstantConditions")

package com.biggestAsk.ui.homeScreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.MainActivity
import com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph.*
import com.biggestAsk.ui.homeScreen.drawerScreens.community.AddCommunityDialog
import com.biggestAsk.ui.homeScreen.drawerScreens.notification.NotificationDetailScreenRoute
import com.biggestAsk.ui.homeScreen.drawerScreens.settingScreens.SettingSubScreen
import com.biggestAsk.ui.introScreen.findActivity
import com.biggestAsk.ui.main.viewmodel.BottomHomeViewModel
import com.biggestAsk.ui.main.viewmodel.BottomMilestoneViewModel
import com.biggestAsk.ui.main.viewmodel.EditMilestoneViewModel
import com.biggestAsk.ui.main.viewmodel.MainViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavHostController,
    context: Context,
    homeActivity: HomeActivity,
    mainViewModel: MainViewModel,
    bottomHomeViewModel: BottomHomeViewModel,
    bottomMilestoneViewModel: BottomMilestoneViewModel,
    editMilestoneViewModel: EditMilestoneViewModel
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val openDialogCustomCommunity = remember { mutableStateOf(false) }
    val openDialogCustomContact = remember { mutableStateOf(false) }
    val tfTextFirstCommunity = remember {
        mutableStateOf("")
    }
    val tfTextSecondCommunity = remember {
        mutableStateOf("")
    }
    val tfTextThirdCommunity = remember {
        mutableStateOf("")
    }
    val tfTextFourthCommunity = remember {
        mutableStateOf("")
    }
    val tfTextFirstContact = remember {
        mutableStateOf("")
    }
    val tfTextSecondContact = remember {
        mutableStateOf("")
    }
    val tfTextThirdContact = remember {
        mutableStateOf("")
    }
    val tfTextFourthContact = remember {
        mutableStateOf("")
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            currentRoute(navController = navController, viewModel = mainViewModel)
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(top = 10.dp)
                    .background(Color.White)
            ) {
                val (icon_open_drawer, tv_tittle_toolbar, icon_add) = createRefs()
                Image(
                    modifier = Modifier
                        .padding(start = 24.dp)
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ) {
                            if (!mainViewModel.isAddMilestoneScreen.value) {
                                scope.launch {
                                    scaffoldState.drawerState.open()
                                }
                            }
                            if (mainViewModel.isAddMilestoneScreen.value) {
                                navController.popBackStack(
                                    BottomNavScreen.AddNewMileStones.route,
                                    true
                                )
                                navController.navigate(BottomNavItems.Milestones.navRoute)
                                navController.popBackStack(BottomNavScreen.MileStones.route, true)
                            }
                            if (mainViewModel.isNotificationDetailsScreen.value) {
                                navController.popBackStack(
                                    NotificationDetailScreenRoute.NotificationDetails.route,
                                    true
                                )
                                navController.navigate(NavDrawerItem.Notifications.route)
                                navController.popBackStack(NavDrawerItem.Notifications.route, true)
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }
                            }
                            if (mainViewModel.isSettingSubAboutAppScreen.value) {
                                navController.popBackStack(
                                    SettingSubScreen.AboutApp.route,
                                    true
                                )
                                navController.navigate(NavDrawerItem.Settings.route)
                                navController.popBackStack(NavDrawerItem.Settings.route, true)
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }
                            }
                            if (mainViewModel.isSettingSubDetailedSettingScreen.value) {
                                navController.popBackStack(
                                    SettingSubScreen.DetailedSetting.route,
                                    true
                                )
                                navController.navigate(NavDrawerItem.Settings.route)
                                navController.popBackStack(NavDrawerItem.Settings.route, true)
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }
                            }
                            if (mainViewModel.isSettingSubPrivacyPolicyScreen.value) {
                                navController.popBackStack(
                                    SettingSubScreen.PrivacyPolicy.route,
                                    true
                                )
                                navController.navigate(NavDrawerItem.Settings.route)
                                navController.popBackStack(NavDrawerItem.Settings.route, true)
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }
                            }
                            if (mainViewModel.isSettingSubTermsOfServiceScreen.value) {
                                navController.popBackStack(
                                    SettingSubScreen.TermsOfService.route,
                                    true
                                )
                                navController.navigate(NavDrawerItem.Settings.route)
                                navController.popBackStack(NavDrawerItem.Settings.route, true)
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }
                            }
                        }
                        .constrainAs(icon_open_drawer) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        },
                    painter = painterResource(
                        id = when (true) {
                            mainViewModel.isAddMilestoneScreen.value -> {
                                R.drawable.ic_baseline_arrow_back_ios_new_24
                            }
                            mainViewModel.isNotificationDetailsScreen.value -> {
                                R.drawable.ic_icon_back_notification_tb
                            }
                            (mainViewModel.isSettingSubAboutAppScreen.value ||
                                    mainViewModel.isSettingSubDetailedSettingScreen.value ||
                                    mainViewModel.isSettingSubPrivacyPolicyScreen.value ||
                                    mainViewModel.isSettingSubTermsOfServiceScreen.value) -> {
                                R.drawable.ic_baseline_arrow_back_ios_new_24
                            }
                            else -> {
                                R.drawable.ic_img_nav_drawer_open_logo
                            }
                        }
                    ),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier.constrainAs(tv_tittle_toolbar) {
                        top.linkTo(parent.top)
                        start.linkTo(icon_open_drawer.end)
                        end.linkTo(icon_add.start)
                        bottom.linkTo(parent.bottom)
                    },
                    text = mainViewModel.toolbarTittle,
                    style = MaterialTheme.typography.body2,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W900,
                    lineHeight = 24.sp
                )
                Icon(
                    modifier = Modifier
                        .padding(end = 24.dp)
                        .width(24.dp)
                        .height(24.dp)
                        .alpha(
                            if (mainViewModel.isCommunityScreen.value == true ||
                                mainViewModel.isContactProvidersScreen.value == true ||
                                mainViewModel.isYourAccountScreen.value == true ||
                                mainViewModel.isNotificationScreen.value == true
                            ) 1f else 0f
                        )
                        .constrainAs(icon_add)
                        {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ) {
                            if (mainViewModel.isCommunityScreen.value == true) {
                                openDialogCustomCommunity.value = true
                            }
                            if (mainViewModel.isContactProvidersScreen.value == true) {
                                openDialogCustomContact.value = true
                            }
                            if (mainViewModel.isYourAccountScreen.value == true) {
                                mainViewModel.isEditable.value =
                                    mainViewModel.isEditable.value != true
                            }
//                            if (mainViewModel.isNotificationScreen.value == true) {
//
//                            }
                        },
                    painter = painterResource(
                        id = when (true) {
                            mainViewModel.isCommunityScreen.value -> {
                                R.drawable.ic_icon_toolbar_add
                            }
                            mainViewModel.isContactProvidersScreen.value -> {
                                R.drawable.ic_icon_toolbar_add
                            }
                            mainViewModel.isYourAccountScreen.value -> {
                                R.drawable.ic_icon_your_account_edit_disable
                            }
                            mainViewModel.isNotificationScreen.value -> {
                                R.drawable.ic_baseline_search_24
                            }
                            else -> {
                                R.drawable.ic_icon_toolbar_add
                            }
                        }
                    ),
                    contentDescription = "",
                    tint = if (mainViewModel.isEditable.value) Custom_Blue else Color.Black
                )
            }
            if (openDialogCustomCommunity.value) {
                Dialog(properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = false,
                    usePlatformDefaultWidth = true,
                ), onDismissRequest = { openDialogCustomCommunity.value = false }) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        AddCommunityDialog(
                            openDialogCustomCommunity,
                            tv_text_tittle = "Create Community",
                            tf_hint_tv1 = "The Happy Agency",
                            tf_hint_tv2 = "Jane Doe",
                            tv_text_second = "Description",
                            tv_text_third = "Link to Forum",
                            tf_hint_tv3 = "Jane Doe",
                            tv_text_fourth = "Link to Instagram",
                            tf_hint_tv4 = "(222)-333-4444",
                            btn_text_add = "+ Add a new Community",
                            tf_text_first = tfTextFirstCommunity,
                            tf_text_second = tfTextSecondCommunity,
                            tf_text_third = tfTextThirdCommunity,
                            tf_text_fourth = tfTextFourthCommunity
                        )
                    }
                }
            }
            if (openDialogCustomContact.value) {
                Dialog(properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = false,
                    usePlatformDefaultWidth = true,
                ),
                    onDismissRequest = { openDialogCustomContact.value = false }) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        AddCommunityDialog(
                            openDialogCustomContact,
                            tv_text_tittle = "Create Contact",
                            tf_hint_tv1 = "The Happy Agency",
                            tf_hint_tv2 = "Jane Doe",
                            tv_text_second = "Agency rep name",
                            tv_text_third = "Agency email",
                            tf_hint_tv3 = "agencyemail@gmail.ua",
                            tv_text_fourth = "Agency phone number",
                            tf_hint_tv4 = "(222)-333-4444",
                            btn_text_add = "+ Add a new Contact",
                            tf_text_first = tfTextFirstContact,
                            tf_text_second = tfTextSecondContact,
                            tf_text_third = tfTextThirdContact,
                            tf_text_fourth = tfTextFourthContact
                        )
                    }
                }
            }
        },
        content = {
            BottomNavigationDrawerGraph(
                navHostController = navController,
                mainViewModel = mainViewModel,
                context = context,
                homeActivity = homeActivity,
                bottomHomeViewModel = bottomHomeViewModel,
                bottomMilestoneViewModel = bottomMilestoneViewModel,
                editMilestoneViewModel = editMilestoneViewModel
            )
        },
        bottomBar = {
            BottomNavigation(
                navController = navController, viewModel = mainViewModel
            )
        },
        drawerBackgroundColor = Color(0xFFF8F5F2),
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            NavigationDrawerContent(
                navController = navController,
                scaffoldState = scaffoldState,
                scope = scope,
                context = context
            )
        })
}

@Composable
fun currentRoute(
    navController: NavHostController,
    viewModel: MainViewModel,
): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    when (navController.currentDestination?.route) {
        BottomNavItems.Home.navRoute -> {
            viewModel.toolbarTittle = "Home"
//            viewModel.list = viewModel.emptyList
            viewModel.isCommunityScreen.value = false
            viewModel.isContactProvidersScreen.value = false
            viewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            viewModel.isNotificationScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
        }
        BottomNavItems.Questions.navRoute -> {
            viewModel.imageList.clear()
            viewModel.toolbarTittle = "Questions"
//            viewModel.list = viewModel.emptyList
            viewModel.isCommunityScreen.value = false
            viewModel.isContactProvidersScreen.value = false
            viewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            viewModel.isNotificationScreen.value = false
            viewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
        }
        BottomNavItems.Milestones.navRoute -> {
            viewModel.imageList.clear()
            viewModel.listData.forEachIndexed { index, _ ->
                viewModel.listData[index].show = false
            }
            viewModel.isSelected = false
            viewModel.toolbarTittle = "Milestones"
            viewModel.isCommunityScreen.value = false
            viewModel.isContactProvidersScreen.value = false
            viewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            viewModel.isNotificationScreen.value = false
            viewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
        }
        NavDrawerItem.YourSurrogateMother.route -> {
            viewModel.imageList.clear()
//            viewModel.list = viewModel.emptyList
            viewModel.toolbarTittle = "Your Surrogate Mother"
            viewModel.isCommunityScreen.value = false
            viewModel.isContactProvidersScreen.value = false
            viewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            viewModel.isNotificationScreen.value = false
            viewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
        }
        NavDrawerItem.IntendedParents.route -> {
            viewModel.imageList.clear()
//            viewModel.list = viewModel.emptyList
            viewModel.toolbarTittle = "Intended Parents"
            viewModel.isCommunityScreen.value = false
            viewModel.isContactProvidersScreen.value = false
            viewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            viewModel.isNotificationScreen.value = false
            viewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
        }
        NavDrawerItem.Community.route -> {
            viewModel.toolbarTittle = "Community"
//            viewModel.list = viewModel.emptyList
            viewModel.isContactProvidersScreen.value = false
            viewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            viewModel.isNotificationScreen.value = false
            viewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
            viewModel.isCommunityScreen.value = true
        }
        NavDrawerItem.ContactYourProviders.route -> {
            viewModel.toolbarTittle = "Contact Your Providers"
//            viewModel.list = viewModel.emptyList
            viewModel.isCommunityScreen.value = false
            viewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            viewModel.isNotificationScreen.value = false
            viewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
            viewModel.isContactProvidersScreen.value = true
        }
        NavDrawerItem.Notifications.route -> {
            viewModel.toolbarTittle = "Notifications"
//            viewModel.list = viewModel.emptyList
            viewModel.isCommunityScreen.value = false
            viewModel.isContactProvidersScreen.value = false
            viewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            viewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
            viewModel.isNotificationScreen.value = true
        }
        NavDrawerItem.Settings.route -> {
            viewModel.toolbarTittle = "Settings"
//            viewModel.list = viewModel.emptyList
            viewModel.isCommunityScreen.value = false
            viewModel.isContactProvidersScreen.value = false
            viewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            viewModel.isNotificationScreen.value = false
            viewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
        }
        MyAccount.MyAccountScreen.route -> {
            viewModel.toolbarTittle = "Your Account"
//            viewModel.list = viewModel.emptyList
            viewModel.isCommunityScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isContactProvidersScreen.value = false
            viewModel.isAddMilestoneScreen.value = false
            viewModel.isNotificationScreen.value = false
            viewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
            viewModel.isYourAccountScreen.value = true
        }
        BottomNavScreen.AddNewMileStones.route -> {
            viewModel.toolbarTittle = "Edit milestone"
//            viewModel.list = viewModel.emptyList
            viewModel.isYourAccountScreen.value = false
            viewModel.isCommunityScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isContactProvidersScreen.value = false
            viewModel.isNotificationScreen.value = false
            viewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
            viewModel.isAddMilestoneScreen.value = true
        }
        NotificationDetailScreenRoute.NotificationDetails.route -> {
            viewModel.toolbarTittle = "Notifications"
//            viewModel.list = viewModel.emptyList
            viewModel.isCommunityScreen.value = false
            viewModel.isContactProvidersScreen.value = false
            viewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            viewModel.isNotificationScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isNotificationDetailsScreen.value = true
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
        }
        SettingSubScreen.AboutApp.route -> {
            viewModel.toolbarTittle = "About App"
//            viewModel.list = viewModel.emptyList
            viewModel.isCommunityScreen.value = false
            viewModel.isContactProvidersScreen.value = false
            viewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            viewModel.isNotificationScreen.value = false
            viewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = true
            viewModel.isSettingSubDetailedSettingScreen.value = false
        }
        SettingSubScreen.DetailedSetting.route -> {
            viewModel.toolbarTittle = "Detailed Settings"
//            viewModel.list = viewModel.emptyList
            viewModel.isCommunityScreen.value = false
            viewModel.isContactProvidersScreen.value = false
            viewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            viewModel.isNotificationScreen.value = false
            viewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = true
        }
        SettingSubScreen.PrivacyPolicy.route -> {
            viewModel.toolbarTittle = "Privacy Policy"
//            viewModel.list = viewModel.emptyList
            viewModel.isCommunityScreen.value = false
            viewModel.isContactProvidersScreen.value = false
            viewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            viewModel.isNotificationScreen.value = false
            viewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = true
        }
        SettingSubScreen.TermsOfService.route -> {
            viewModel.toolbarTittle = "Terms of Service"
//            viewModel.list = viewModel.emptyList
            viewModel.isCommunityScreen.value = false
            viewModel.isContactProvidersScreen.value = false
            viewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            viewModel.isNotificationScreen.value = false
            viewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = true
        }
    }
    return navBackStackEntry?.arguments?.getString("Community")
}

@Composable
fun NavigationDrawerContent(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    context: Context
) {
    val navDrawerItems = mutableListOf(
        NavDrawerItem.YourSurrogateMother,
        NavDrawerItem.Community,
        NavDrawerItem.ContactYourProviders,
        NavDrawerItem.Notifications,
        NavDrawerItem.Settings
    )
    Column(
        modifier = Modifier
            .padding(start = 24.dp, top = 40.dp)
    ) {
        Row(
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
            ) {
                navController.navigate(MyAccount.MyAccountScreen.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            }
        ) {
            Image(
                modifier = Modifier
                    .width(56.dp)
                    .height(56.dp),
                painter = painterResource(id = R.drawable.img_nav_drawer),
                contentDescription = ""
            )
            Column {
                Text(
                    text = "Mark Baggins", modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = 13.dp),
                    style = MaterialTheme.typography.body2,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W600,
                    lineHeight = 32.sp,
                    color = Color.Black
                )
                Text(
                    text = "Parents", modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = 13.dp, top = 5.dp),
                    style = MaterialTheme.typography.body1,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    lineHeight = 24.sp,
                    color = Custom_Blue
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(top = 40.dp)
        ) {
            navDrawerItems.forEach { item ->
                Row(
                    modifier = Modifier
                        .padding(top = 35.dp)
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }
                            }
                        }
                ) {
                    Icon(
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp),
                        painter = painterResource(id = item.icon),
                        contentDescription = ""
                    )
                    Text(
                        text = item.tittle,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(start = 16.dp, top = 2.dp),
                        style = MaterialTheme.typography.body1,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp)
        ) {
            val checkedState = remember { mutableStateOf(true) }
            Text(
                text = "Show Pregnancy Milestone",
                style = MaterialTheme.typography.body1,
                fontSize = 16.sp,
                color = Color.Black
            )
            Switch(
                modifier = Modifier
                    .height(25.dp)
                    .padding(start = 35.dp),
                checked = checkedState.value,
                onCheckedChange = { checkedState.value = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Custom_Blue,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Custom_Blue
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp),
                painter = painterResource(id = R.drawable.ic_icon_nav_drawer_logout),
                contentDescription = ""
            )
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 3.dp)
                    .clickable(indication = null, interactionSource = MutableInteractionSource()) {
//                        context.startActivity(Intent(context.applicationContext.findActivity(), MainActivity::class.java))
//                        context.applicationContext
//                            .findActivity()
//                            ?.finish()
//                        PreferenceProvider(context).clear()
                    },
                text = "Log out",
                style = MaterialTheme.typography.body1,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

object ClearRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color = Color.Transparent

    @Composable
    override fun rippleAlpha() = RippleAlpha(
        draggedAlpha = 0.0f,
        focusedAlpha = 0.0f,
        hoveredAlpha = 0.0f,
        pressedAlpha = 0.0f,
    )
}

@Composable
fun BottomNavigation(navController: NavController, viewModel: MainViewModel) {
    val navItems = listOf(BottomNavItems.Milestones, BottomNavItems.Home, BottomNavItems.Questions)
    CompositionLocalProvider(
        LocalRippleTheme provides ClearRippleTheme
    ) {
        BottomNavigation(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            val backStackEntry = navController.currentBackStackEntryAsState()
            navItems.forEach { item ->
                val selected = item.navRoute == backStackEntry.value?.destination?.route
                BottomNavigationItem(
                    modifier = Modifier.background(Color.White),
                    label = {
                        Text(
                            text = item.tittle,
                            style = MaterialTheme.typography.body2,
                            fontStyle = FontStyle.Normal,
                            fontSize = 12.sp,
                            color = if (selected) Color(0xFFAE4B2B) else Color(0xFF666463)
                        )
                    },
                    alwaysShowLabel = true,
                    selected = selected,
                    selectedContentColor = Color.Black,
                    icon = {
                        if (selected) Image(
                            painter = painterResource(id = item.selectedIcon),
                            contentDescription = "",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) else Image(
                            painter = painterResource(id = item.defaultIcon),
                            contentDescription = "",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(bottom = 4.dp)
                        )
                    },
                    onClick = {
                        viewModel.toolbarTittle = item.tittle
                        navController.navigate(item.navRoute) {
                            if (item.navRoute == BottomNavItems.Home.navRoute) {
                                viewModel.isCommunityScreen.value = false
                            }
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
//                            restoreState = true
                        }
                    },
                )
            }
        }
    }
}

