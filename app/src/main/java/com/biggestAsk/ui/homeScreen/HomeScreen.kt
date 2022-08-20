@file:Suppress("OPT_IN_IS_NOT_ENABLED", "KotlinConstantConditions")

package com.biggestAsk.ui.homeScreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.biggestAsk.ui.homeScreen.bottomNavScreen.BackHandler
import com.biggestAsk.ui.homeScreen.drawerScreens.community.AddCommunityDialog
import com.biggestAsk.ui.homeScreen.drawerScreens.contactYourProvider.CreateContactDialog
import com.biggestAsk.ui.homeScreen.drawerScreens.notification.NotificationDetailScreenRoute
import com.biggestAsk.ui.homeScreen.drawerScreens.settingScreens.SettingSubScreen
import com.biggestAsk.ui.homeScreen.drawerScreens.yourAccount.LogoutDialog
import com.biggestAsk.ui.introScreen.findActivity
import com.biggestAsk.ui.main.viewmodel.*
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavHostController,
    context: Context,
    homeActivity: HomeActivity,
    mainViewModel: MainViewModel,
    bottomHomeViewModel: BottomHomeViewModel,
    bottomMilestoneViewModel: BottomMilestoneViewModel,
    editMilestoneViewModel: EditMilestoneViewModel,
    yourAccountViewModel: YourAccountViewModel,
    contactYourProviderViewModel: ContactYourProviderViewModel,
    surrogateViewModel: YourSurrogateViewModel,
    communityViewModel: CommunityViewModel,
    notificationViewModel: NotificationViewModel,
    aboutAppViewModel: AboutAppViewModel,
    yourSurrogateMotherViewModel: YourSurrogateMotherViewModel,
    intendedParentsViewModel: IntendedParentsViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val requester = FocusRequester()
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
            currentRoute(
                navController = navController,
                viewModel = mainViewModel,
                yourAccountViewModel = yourAccountViewModel,
                contactYourProviderViewModel = contactYourProviderViewModel,
                communityViewModel = communityViewModel,
                notificationViewModel = notificationViewModel
            )
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(top = 10.dp)
                    .background(Color.White)
            ) {
                val (icon_open_drawer, tv_tittle_toolbar, icon_add, text_field_search) = createRefs()
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
                            if (notificationViewModel.isNotificationDetailsScreen.value) {
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
                            notificationViewModel.isNotificationDetailsScreen.value -> {
                                R.drawable.ic_icon_back_notification_tb
                            }
                            (mainViewModel.isSettingSubAboutAppScreen.value ||
                                    mainViewModel.isSettingSubDetailedSettingScreen.value ||
                                    mainViewModel.isSettingSubPrivacyPolicyScreen.value ||
                                    mainViewModel.isSettingSubTermsOfServiceScreen.value),
                            -> {
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
                    modifier = Modifier
                        .constrainAs(tv_tittle_toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(icon_open_drawer.end)
                            end.linkTo(icon_add.start)
                            bottom.linkTo(parent.bottom)
                        }
                        .alpha(if (notificationViewModel.isNotificationScreen.value == true &&
                            notificationViewModel.isSearchClicked.value
                        ) 0f else 1f),
                    text = mainViewModel.toolbarTittle,
                    style = MaterialTheme.typography.body2,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W900,
                    lineHeight = 24.sp
                )
                if (notificationViewModel.isNotificationScreen.value == true && notificationViewModel.isSearchClicked.value) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(text_field_search) {
                            top.linkTo(parent.top)
                            start.linkTo(icon_open_drawer.end)
                            end.linkTo(icon_add.start)
                            bottom.linkTo(parent.bottom)
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(145.dp)) {
                        BasicTextField(
                            modifier = Modifier
                                .padding(start = 55.dp)
                                .focusRequester(requester)
                                .onFocusChanged {
                                    if (!it.isFocused) {
                                        requester.requestFocus()
                                        keyboardController?.show()
                                    }
                                }
                                .background(Color.White),
                            value = notificationViewModel.searchText,
                            onValueChange = { it1 ->
                                notificationViewModel.updatedList.clear()
                                notificationViewModel.searchText = it1
                                notificationViewModel.getFilteredList(notificationViewModel.searchText)
                            },
                            singleLine = true,
                            decorationBox = { innerText ->
                                if (notificationViewModel.searchText == "") {
                                    Text(text = "Search ", modifier = Modifier.fillMaxWidth())
                                } else {
                                    innerText()
                                }
                            },
                        )
                        IconButton(
                            onClick = {
                                notificationViewModel.searchText =
                                    "" // Remove text from TextField when you press the 'X' icon
                                notificationViewModel.isNotificationScreen.value = true
                                notificationViewModel.isSearchClicked.value = false
                                notificationViewModel.updatedList.clear()
                                notificationViewModel.updatedList.addAll(notificationViewModel.notificationList)
                            }
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                }
                Icon(
                    modifier = Modifier
                        .padding(end = 24.dp)
                        .width(24.dp)
                        .height(24.dp)
                        .alpha(
                            if (communityViewModel.isCommunityScreen.value == true ||
                                contactYourProviderViewModel.isContactProvidersScreen.value == true ||
                                yourAccountViewModel.isYourAccountScreen.value == true ||
                                (notificationViewModel.isNotificationScreen.value == true && !notificationViewModel.isSearchClicked.value)
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
                            if (communityViewModel.isCommunityScreen.value == true) {
                                openDialogCustomCommunity.value = true
                            }
                            if (contactYourProviderViewModel.isContactProvidersScreen.value == true) {
                                openDialogCustomContact.value = true
                            }
                            if (yourAccountViewModel.isYourAccountScreen.value == true) {
                                yourAccountViewModel.isEditable.value =
                                    yourAccountViewModel.isEditable.value != true
                                Log.d(
                                    "TAG",
                                    "HomeScreen: isEditable ${yourAccountViewModel.isEditable.value}"
                                )
                            }
                            if (notificationViewModel.isNotificationScreen.value == true) {
//                                if (notificationViewModel.searchText != ""){
//                                    notificationViewModel.searchText = ""
//                                }
                                if (!notificationViewModel.isSearchClicked.value) {
//                                    notificationViewModel.searchText = ""
                                    notificationViewModel.isSearchClicked.value =
                                        notificationViewModel.isSearchClicked.value != true
                                }
                            }
                        },
                    painter = painterResource(
                        id = when (true) {
                            communityViewModel.isCommunityScreen.value -> {
                                R.drawable.ic_icon_toolbar_add
                            }
                            contactYourProviderViewModel.isContactProvidersScreen.value -> {
                                R.drawable.ic_icon_toolbar_add
                            }
                            yourAccountViewModel.isYourAccountScreen.value -> {
                                R.drawable.ic_icon_your_account_edit_disable
                            }
                            notificationViewModel.isNotificationScreen.value -> {
                                R.drawable.ic_baseline_search_24
                            }
                            else -> {
                                R.drawable.ic_icon_toolbar_add
                            }
                        }
                    ),
                    contentDescription = "",
                    tint = if (yourAccountViewModel.isEditable.value) Custom_Blue else Color.Black
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
                            homeActivity = homeActivity,
                            communityViewModel = communityViewModel,
                            openDialogCustomCommunity,
                            tv_text_tittle = stringResource(id = R.string.create_community),
                            tf_hint_tv1 = stringResource(id = R.string.the_happy_agency),
                            tf_hint_tv2 = stringResource(id = R.string.jane_doe),
                            tv_text_second = stringResource(id = R.string.description),
                            tv_text_third = stringResource(id = R.string.link_to_forum),
                            tf_hint_tv3 = stringResource(id = R.string.jane_doe),
                            tv_text_fourth = stringResource(id = R.string.link_to_instagram),
                            tf_hint_tv4 = stringResource(id = R.string.instagram_hint),
                            btn_text_add = stringResource(id = R.string.add_community),
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
                        CreateContactDialog(
                            homeActivity = homeActivity,
                            contactYourProviderViewModel = contactYourProviderViewModel,
                            openDialogCustomContact,
                            tv_text_tittle = stringResource(id = R.string.create_contact),
                            tf_hint_tv1 = stringResource(id = R.string.the_happy_agency),
                            tf_hint_tv2 = stringResource(id = R.string.jane_doe),
                            tv_text_second = stringResource(id = R.string.agency_rep_name),
                            tv_text_third = stringResource(id = R.string.agency_email),
                            tf_hint_tv3 = stringResource(id = R.string.agency_email_hint),
                            tv_text_fourth = stringResource(id = R.string.agency_phone_number),
                            tf_hint_tv4 = stringResource(id = R.string.dialog_phone),
                            btn_text_add = stringResource(id = R.string.add_contact),
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
                editMilestoneViewModel = editMilestoneViewModel,
                yourAccountViewModel = yourAccountViewModel,
                surrogateViewModel = surrogateViewModel,
                contactYourProviderViewModel = contactYourProviderViewModel,
                communityViewModel = communityViewModel,
                notificationViewModel = notificationViewModel,
                aboutAppViewModel = aboutAppViewModel,
                yourSurrogateMotherViewModel = yourSurrogateMotherViewModel,
                intendedParentsViewModel = intendedParentsViewModel,
                scaffoldState = scaffoldState
            )
            BackHandler(scaffoldState.drawerState.isOpen) {
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }
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
                context = context,
                homeActivity = homeActivity
            )
        })
}

@Composable
fun currentRoute(
    navController: NavHostController,
    viewModel: MainViewModel,
    yourAccountViewModel: YourAccountViewModel,
    contactYourProviderViewModel: ContactYourProviderViewModel,
    communityViewModel: CommunityViewModel,
    notificationViewModel: NotificationViewModel,
): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    when (navController.currentDestination?.route) {
        BottomNavItems.Home.navRoute -> {
            viewModel.toolbarTittle = stringResource(id = R.string.text_home_bottom_nav_home)
            //            viewModel.list = viewModel.emptyList
            communityViewModel.isCommunityScreen.value = false
            contactYourProviderViewModel.isContactProvidersScreen.value = false
            yourAccountViewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            yourAccountViewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            notificationViewModel.isNotificationScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
        }
        BottomNavItems.Questions.navRoute -> {
            //            viewModel.imageList.clear()
            viewModel.toolbarTittle = stringResource(id = R.string.text_home_bottom_nav_question)
            //            viewModel.list = viewModel.emptyList
            communityViewModel.isCommunityScreen.value = false
            contactYourProviderViewModel.isContactProvidersScreen.value = false
            yourAccountViewModel.isEditable.value = false
            yourAccountViewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            notificationViewModel.isNotificationScreen.value = false
            notificationViewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
        }
        BottomNavItems.Milestones.navRoute -> {
            //            viewModel.imageList.clear()
            //            viewModel.listData.forEachIndexed { index, _ ->
            //                viewModel.listData[index].show = false
            //            }
            // viewModel.isSelected = false
            viewModel.toolbarTittle = stringResource(id = R.string.text_home_bottom_nav_milestone)
            communityViewModel.isCommunityScreen.value = false
            yourAccountViewModel.isEditable.value = false
            contactYourProviderViewModel.isContactProvidersScreen.value = false
            yourAccountViewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            notificationViewModel.isNotificationScreen.value = false
            notificationViewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
        }
        NavDrawerItem.YourSurrogateMother.route -> {
            //            viewModel.imageList.clear()
            //            viewModel.list = viewModel.emptyList
            viewModel.toolbarTittle = stringResource(id = R.string.your_surrogate_mother)
            communityViewModel.isCommunityScreen.value = false
            contactYourProviderViewModel.isContactProvidersScreen.value = false
            yourAccountViewModel.isEditable.value = false
            yourAccountViewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            notificationViewModel.isNotificationScreen.value = false
            notificationViewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
        }
        NavDrawerItem.IntendedParents.route -> {
            //            viewModel.imageList.clear()
            //            viewModel.list = viewModel.emptyList
            viewModel.toolbarTittle = stringResource(id = R.string.intended_parents)
            communityViewModel.isCommunityScreen.value = false
            yourAccountViewModel.isEditable.value = false
            contactYourProviderViewModel.isContactProvidersScreen.value = false
            yourAccountViewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            notificationViewModel.isNotificationScreen.value = false
            notificationViewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
        }
        NavDrawerItem.Community.route -> {
            viewModel.toolbarTittle = stringResource(id = R.string.community)
            //            viewModel.list = viewModel.emptyList
            contactYourProviderViewModel.isContactProvidersScreen.value = false
            yourAccountViewModel.isYourAccountScreen.value = false
            yourAccountViewModel.isEditable.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            notificationViewModel.isNotificationScreen.value = false
            notificationViewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
            communityViewModel.isCommunityScreen.value = true
        }
        NavDrawerItem.ContactYourProviders.route -> {
            viewModel.toolbarTittle = stringResource(id = R.string.contact_your_providers)
            //            viewModel.list = viewModel.emptyList
            communityViewModel.isCommunityScreen.value = false
            yourAccountViewModel.isYourAccountScreen.value = false
            yourAccountViewModel.isEditable.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            notificationViewModel.isNotificationScreen.value = false
            notificationViewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
            contactYourProviderViewModel.isContactProvidersScreen.value = true
        }
        NavDrawerItem.Notifications.route -> {
            viewModel.toolbarTittle = stringResource(id = R.string.notifications)
            //            viewModel.list = viewModel.emptyList
            communityViewModel.isCommunityScreen.value = false
            notificationViewModel.isSearchClicked.value = false
            contactYourProviderViewModel.isContactProvidersScreen.value = false
            yourAccountViewModel.isYourAccountScreen.value = false
            yourAccountViewModel.isEditable.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            notificationViewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
            notificationViewModel.searchText = ""
            notificationViewModel.isNotificationScreen.value = true
        }
        NavDrawerItem.Settings.route -> {
            viewModel.toolbarTittle = stringResource(id = R.string.settings)
            //            viewModel.list = viewModel.emptyList
            communityViewModel.isCommunityScreen.value = false
            contactYourProviderViewModel.isContactProvidersScreen.value = false
            yourAccountViewModel.isYourAccountScreen.value = false
            yourAccountViewModel.isEditable.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            notificationViewModel.isNotificationScreen.value = false
            notificationViewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
        }
        MyAccount.MyAccountScreen.route -> {
            viewModel.toolbarTittle = stringResource(id = R.string.your_account)
            //            viewModel.list = viewModel.emptyList
            communityViewModel.isCommunityScreen.value = false
            viewModel.isEditable.value = false
            contactYourProviderViewModel.isContactProvidersScreen.value = false
            viewModel.isAddMilestoneScreen.value = false
            notificationViewModel.isNotificationScreen.value = false
            notificationViewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
            yourAccountViewModel.isYourAccountScreen.value = true
        }
        BottomNavScreen.AddNewMileStones.route -> {
            viewModel.toolbarTittle = stringResource(id = R.string.edit_milestone)
            //            viewModel.list = viewModel.emptyList
            yourAccountViewModel.isYourAccountScreen.value = false
            communityViewModel.isCommunityScreen.value = false
            yourAccountViewModel.isEditable.value = false
            viewModel.isEditable.value = false
            contactYourProviderViewModel.isContactProvidersScreen.value = false
            notificationViewModel.isNotificationScreen.value = false
            notificationViewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
            viewModel.isAddMilestoneScreen.value = true
        }
        NotificationDetailScreenRoute.NotificationDetails.route -> {
            viewModel.toolbarTittle = stringResource(id = R.string.notifications)
            //            viewModel.list = viewModel.emptyList
            communityViewModel.isCommunityScreen.value = false
            yourAccountViewModel.isEditable.value = false
            contactYourProviderViewModel.isContactProvidersScreen.value = false
            yourAccountViewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            notificationViewModel.isNotificationScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            notificationViewModel.isNotificationDetailsScreen.value = true
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
        }
        SettingSubScreen.AboutApp.route -> {
            viewModel.toolbarTittle = stringResource(id = R.string.about_app)
            //            viewModel.list = viewModel.emptyList
            communityViewModel.isCommunityScreen.value = false
            contactYourProviderViewModel.isContactProvidersScreen.value = false
            yourAccountViewModel.isYourAccountScreen.value = false
            yourAccountViewModel.isEditable.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            notificationViewModel.isNotificationScreen.value = false
            notificationViewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = true
            viewModel.isSettingSubDetailedSettingScreen.value = false
        }
        SettingSubScreen.DetailedSetting.route -> {
            viewModel.toolbarTittle = stringResource(id = R.string.detailed_settings)
            //            viewModel.list = viewModel.emptyList
            communityViewModel.isCommunityScreen.value = false
            contactYourProviderViewModel.isContactProvidersScreen.value = false
            yourAccountViewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            yourAccountViewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            notificationViewModel.isNotificationScreen.value = false
            notificationViewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = true
        }
        SettingSubScreen.PrivacyPolicy.route -> {
            viewModel.toolbarTittle = stringResource(id = R.string.privacy_policy)
            //            viewModel.list = viewModel.emptyList
            communityViewModel.isCommunityScreen.value = false
            contactYourProviderViewModel.isContactProvidersScreen.value = false
            yourAccountViewModel.isYourAccountScreen.value = false
            yourAccountViewModel.isEditable.value = false
            viewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            notificationViewModel.isNotificationScreen.value = false
            notificationViewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = false
            viewModel.isSettingSubPrivacyPolicyScreen.value = true
        }
        SettingSubScreen.TermsOfService.route -> {
            viewModel.toolbarTittle = stringResource(id = R.string.terms_of_service)
            //            viewModel.list = viewModel.emptyList
            communityViewModel.isCommunityScreen.value = false
            contactYourProviderViewModel.isContactProvidersScreen.value = false
            yourAccountViewModel.isYourAccountScreen.value = false
            viewModel.isEditable.value = false
            yourAccountViewModel.isEditable.value = false
            viewModel.isAddMilestoneScreen.value = false
            notificationViewModel.isNotificationScreen.value = false
            notificationViewModel.isNotificationDetailsScreen.value = false
            viewModel.isSettingSubAboutAppScreen.value = false
            viewModel.isSettingSubDetailedSettingScreen.value = false
            viewModel.isSettingSubTermsOfServiceScreen.value = true
        }
    }
    return navBackStackEntry?.arguments?.getString(Constants.COMMUNITY_TITTLE)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NavigationDrawerContent(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    context: Context,
    homeActivity: HomeActivity,
) {
    val provider = PreferenceProvider(context)
    val type = provider.getValue(Constants.TYPE, "")
    val isSurrogate =
        if (type == Constants.PARENT) NavDrawerItem.YourSurrogateMother else NavDrawerItem.IntendedParents
    val navDrawerItems = mutableListOf(
        isSurrogate,
        NavDrawerItem.Community,
        NavDrawerItem.ContactYourProviders,
        NavDrawerItem.Notifications,
        NavDrawerItem.Settings
    )
    val openLogoutDialog = remember { mutableStateOf(false) }
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
                    text = stringResource(id = R.string.mark_baggins), modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = 13.dp),
                    style = MaterialTheme.typography.body2,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W600,
                    lineHeight = 32.sp,
                    color = Color.Black
                )
                Text(
                    text = stringResource(id = R.string.text_home_bottom_sheet_parent),
                    modifier = Modifier
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
                text = stringResource(id = R.string.show_pregnancy_milestone),
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
                     openLogoutDialog.value = true
                    },
                text = stringResource(id = R.string.log_out),
                style = MaterialTheme.typography.body1,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
        if (openLogoutDialog.value) {
            Dialog(
                onDismissRequest = { openLogoutDialog.value = false },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = false,
                    usePlatformDefaultWidth = true,
                )
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    LogoutDialog(
                        openLogoutDialog = openLogoutDialog,
                        context = context,
                        homeActivity = homeActivity
                    )
                }
            }
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