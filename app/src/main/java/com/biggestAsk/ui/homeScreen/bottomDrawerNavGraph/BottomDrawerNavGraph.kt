package com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.homeScreen.bottomNavScreen.AddNewMilestoneScreen
import com.biggestAsk.ui.homeScreen.bottomNavScreen.BottomHomeScreen
import com.biggestAsk.ui.homeScreen.bottomNavScreen.BottomQuestionScreen
import com.biggestAsk.ui.homeScreen.bottomNavScreen.MilestonesScreen
import com.biggestAsk.ui.homeScreen.drawerScreens.community.Community
import com.biggestAsk.ui.homeScreen.drawerScreens.contactYourProvider.ContactYourProvider
import com.biggestAsk.ui.homeScreen.drawerScreens.intendParents.IntendParentsScreen
import com.biggestAsk.ui.homeScreen.drawerScreens.notification.*
import com.biggestAsk.ui.homeScreen.drawerScreens.settingScreens.*
import com.biggestAsk.ui.homeScreen.drawerScreens.yourAccount.YourAccountScreen
import com.biggestAsk.ui.homeScreen.drawerScreens.yourSurrogateMother.YourSurrogateMother
import com.biggestAsk.ui.main.viewmodel.BottomHomeViewModel
import com.biggestAsk.ui.main.viewmodel.HomeViewModel
import com.biggestAsk.ui.main.viewmodel.MainViewModel

@Composable
fun BottomNavigationDrawerGraph(
    navHostController: NavHostController,
    mainViewModel: MainViewModel,
    context: Context,
    homeActivity: HomeActivity,
    bottomHomeViewModel: BottomHomeViewModel
) {
    NavHost(
        navController = navHostController, startDestination = BottomNavScreen.Home.route
    ) {
        composable(
            route = BottomNavScreen.MileStones.route
        ) {
            MilestonesScreen(navHostController = navHostController, viewModel = mainViewModel)
        }
        composable(
            route = BottomNavScreen.Home.route
        ) {
            BottomHomeScreen(
                navHostController = navHostController,
                context = context,
                homeActivity = homeActivity,
                bottomHomeViewModel = bottomHomeViewModel
            )
        }
        composable(
            route = BottomNavScreen.Question.route
        ) {
            BottomQuestionScreen(navHostController = navHostController)
        }
        composable(
            route = BottomNavScreen.AddNewMileStones.route,
            arguments = listOf(
                navArgument(ADD_NEW_MILESTONE_ARGS_NAME) {
                    type = NavType.StringType
                })
        ) {
            AddNewMilestoneScreen(
                navHostController,
                mainViewModel,
                it.arguments?.getString(ADD_NEW_MILESTONE_ARGS_NAME).toString()
            )
        }
        composable(route = NavDrawerItem.IntendedParents.route) {
            IntendParentsScreen(viewModel = mainViewModel)
        }
        composable(route = NavDrawerItem.YourSurrogateMother.route) {
            YourSurrogateMother(mainViewModel)
        }
        composable(route = NavDrawerItem.Community.route) {
            Community()
        }
        composable(route = NavDrawerItem.ContactYourProviders.route) {
            ContactYourProvider()
        }
        composable(
            route = NavDrawerItem.Notifications.route
        ) {
            Notification(navHostController = navHostController)
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
            AboutApp()
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
            YourAccountScreen(mainViewModel)
        }

    }
}