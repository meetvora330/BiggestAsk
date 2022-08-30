package com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph

import android.content.Context
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.biggestAsk.data.model.LoginStatus
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.homeScreen.bottomNavScreen.BottomHomeScreen
import com.biggestAsk.ui.homeScreen.bottomNavScreen.BottomQuestionScreen
import com.biggestAsk.ui.homeScreen.bottomNavScreen.EditMilestoneScreen
import com.biggestAsk.ui.homeScreen.bottomNavScreen.MilestonesScreen
import com.biggestAsk.ui.homeScreen.drawerScreens.community.Community
import com.biggestAsk.ui.homeScreen.drawerScreens.contactYourProvider.ContactYourProvider
import com.biggestAsk.ui.homeScreen.drawerScreens.intendParents.IntendParentsScreen
import com.biggestAsk.ui.homeScreen.drawerScreens.notification.*
import com.biggestAsk.ui.homeScreen.drawerScreens.settingScreens.*
import com.biggestAsk.ui.homeScreen.drawerScreens.yourAccount.YourAccountScreen
import com.biggestAsk.ui.homeScreen.drawerScreens.yourSurrogateMother.AddSurrogateMother
import com.biggestAsk.ui.homeScreen.drawerScreens.yourSurrogateMother.SurrogateParentNotAssignScreen
import com.biggestAsk.ui.homeScreen.drawerScreens.yourSurrogateMother.YourSurrogateMother
import com.biggestAsk.ui.main.viewmodel.*
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import java.util.*

@Composable
fun BottomNavigationDrawerGraph(
    navHostController: NavHostController,
    mainViewModel: MainViewModel,
    context: Context,
    homeActivity: HomeActivity,
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
    intendedParentsViewModel: IntendedParentsViewModel,
    questionViewModel: BottomQuestionViewModel,
    scaffoldState: ScaffoldState,
    frequencyViewModel: FrequencyViewModel
) {
    NavHost(
        navController = navHostController, startDestination = BottomNavScreen.Home.route
    ) {
        composable(
            route = BottomNavScreen.MileStones.route
        ) {
            MilestonesScreen(
                navHostController = navHostController,
                milestoneViewModel = bottomMilestoneViewModel,
                homeActivity = homeActivity,
                scaffoldState = scaffoldState
            )
        }
        composable(
            route = BottomNavScreen.Home.route
        ) {
            val provider = PreferenceProvider(context)
            val type = provider.getValue(Constants.TYPE, "")
            when (provider.getValue(Constants.LOGIN_STATUS, "")) {
                LoginStatus.PARTNER_NOT_ASSIGN.name.lowercase(Locale.getDefault()) -> {
                    if (type == Constants.PARENT) {
                        AddSurrogateMother(
                            surrogateViewModel = surrogateViewModel,
                            context = context,
                            homeActivity = homeActivity,
                            navHostController
                        )
                    } else {
                        SurrogateParentNotAssignScreen(stringResource(id = R.string.label_surrogate_parent_not_available))
                    }
                }
                LoginStatus.MILESTONE_DATE_NOT_ADDED.name.lowercase(Locale.getDefault()) -> {
                    SurrogateParentNotAssignScreen(stringResource(id = R.string.label_add_milestone_date))
                }
                LoginStatus.ON_BOARDING.name.lowercase(Locale.getDefault()) -> {
                    BottomHomeScreen(
                        context = context,
                        homeActivity = homeActivity,
                        bottomHomeViewModel = bottomHomeViewModel,
                        frequencyViewModel = frequencyViewModel
                    )
                }
            }
        }
        composable(
            route = BottomNavScreen.Question.route
        ) {
            BottomQuestionScreen(
                questionViewModel = questionViewModel,
                context = context,
                homeActivity = homeActivity,
                yourAccountViewModel = yourAccountViewModel,
                frequencyViewModel = frequencyViewModel
            )
        }
        composable(
            route = BottomNavScreen.SurrogateParentNotAssignScreen.route
        ) {
            SurrogateParentNotAssignScreen(stringResource(id = R.string.label_add_milestone_date))
        }
        composable(
            route = BottomNavScreen.AddNewMileStones.route,
            arguments = listOf(
                navArgument(ADD_NEW_MILESTONE_ARGS_ID) {
                    type = NavType.IntType
                })
        ) {
            EditMilestoneScreen(
                navHostController,
                it.arguments?.getInt(ADD_NEW_MILESTONE_ARGS_ID)!!,
                editMilestoneViewModel,
                homeActivity = homeActivity
            )
        }
        composable(route = NavDrawerItem.IntendedParents.route) {
            IntendParentsScreen(
                homeActivity = homeActivity,
                context = context,
                intendedParentsViewModel = intendedParentsViewModel
            )
        }
        composable(route = NavDrawerItem.YourSurrogateMother.route) {
            val provider = PreferenceProvider(context)
            when (provider.getValue(Constants.LOGIN_STATUS, "")) {
                LoginStatus.PARTNER_NOT_ASSIGN.name.lowercase(Locale.getDefault()) -> {
                    AddSurrogateMother(
                        surrogateViewModel = surrogateViewModel,
                        context = context,
                        homeActivity = homeActivity,
                        navHostController
                    )
                }
                else -> {
                    YourSurrogateMother(
                        homeActivity = homeActivity,
                        yourSurrogateMotherViewModel = yourSurrogateMotherViewModel,
                        context = context
                    )
                }
            }

        }
        composable(route = NavDrawerItem.Community.route) {
            Community(
                communityViewModel = communityViewModel,
                homeActivity = homeActivity
            )
        }
        composable(route = NavDrawerItem.ContactYourProviders.route) {
            ContactYourProvider(
                homeActivity = homeActivity,
                contactYourProviderViewModel = contactYourProviderViewModel
            )
        }
        composable(
            route = NavDrawerItem.Notifications.route
        ) {
            Notification(navHostController = navHostController,
                notificationViewModel = notificationViewModel,
                homeActivity = homeActivity)
        }
        composable(
            route = NotificationDetailScreenRoute.NotificationDetails.route, arguments = listOf(
                navArgument(name = NOTIFICATION_DETAILS_ICON_KEY) {
                    type = NavType.IntType
                },
                navArgument(name = NOTIFICATION_DETAILS_TITTLE_KEY) {
                    type = NavType.StringType
                },
                navArgument(name = NOTIFICATION_DETAILS_DESC_KEY) {
                    type = NavType.StringType
                },
                navArgument(name = NOTIFICATION_DETAILS_DAYS_KEY) {
                    type = NavType.StringType
                }
            )
        ) {
            val icon = it.arguments?.getInt(NOTIFICATION_DETAILS_ICON_KEY)
            val title = it.arguments?.getString(NOTIFICATION_DETAILS_TITTLE_KEY)
            val desc = it.arguments?.getString(NOTIFICATION_DETAILS_DESC_KEY)
            val days = it.arguments?.getString(NOTIFICATION_DETAILS_DAYS_KEY)
            NotificationDetails(
                navHostController = navHostController,
                notificationDetailsIcon = icon,
                notificationDetailsTittle = title,
                notificationDetailsDescription = desc,
                notificationDetailsDays = days
            )
        }
        composable(route = NavDrawerItem.Settings.route) {
            Settings(navHostController = navHostController)
        }
        composable(route = SettingSubScreen.AboutApp.route) {
            AboutApp(homeActivity = homeActivity, aboutAppViewModel = aboutAppViewModel)
        }
        composable(route = SettingSubScreen.TermsOfService.route) {
            TermsOfService()
        }
        composable(route = SettingSubScreen.PrivacyPolicy.route) {
            PrivacyPolicy()
        }
        composable(SettingSubScreen.DetailedSetting.route) {
            DetailedSettings()
        }
        composable(route = MyAccount.MyAccountScreen.route) {
            YourAccountScreen(
                navHostController = navHostController,
                yourAccountViewModel = yourAccountViewModel,
                homeActivity = homeActivity,
                context = context
            )
        }

    }
}