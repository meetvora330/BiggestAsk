package com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
import com.biggestAsk.ui.homeScreen.drawerScreens.yourSurrogateMother.SurrogateMotherPresent
import com.biggestAsk.ui.homeScreen.drawerScreens.yourSurrogateMother.YourSurrogateMother
import com.biggestAsk.ui.main.viewmodel.*
import com.biggestAsk.util.PreferenceProvider

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
    surrogateViewModel: YourSurrogateViewModel
) {
    NavHost(
        navController = navHostController, startDestination = BottomNavScreen.Home.route
    ) {
        composable(
            route = BottomNavScreen.MileStones.route
        ) {
            MilestonesScreen(
                navHostController = navHostController,
                viewModel = mainViewModel,
                milestoneViewModel = bottomMilestoneViewModel,
                homeActivity = homeActivity
            )
        }
        composable(
            route = BottomNavScreen.Home.route
        ) {
            val provider = PreferenceProvider(context)
            val type = provider.getValue("type", "")
            val partnerId = provider.getIntValue("partner_id", 0)
            if (type == "parent") {
                Log.d("TAG", "BottomNavigationDrawerGraph: $partnerId")
                if (partnerId == 0) {
                    YourSurrogateMother(surrogateViewModel = surrogateViewModel, context = context, homeActivity = homeActivity)
                }else{
                    BottomHomeScreen(
                        navHostController = navHostController,
                        context = context,
                        homeActivity = homeActivity,
                        bottomHomeViewModel = bottomHomeViewModel
                    )
                }
            } else {
                if (partnerId == 0) {
                    SurrogateMotherPresent()
                }else{
                    BottomHomeScreen(
                        navHostController = navHostController,
                        context = context,
                        homeActivity = homeActivity,
                        bottomHomeViewModel = bottomHomeViewModel
                    )
                }
            }
        }
        composable(
            route = BottomNavScreen.Question.route
        ) {
            BottomQuestionScreen(navHostController = navHostController)
        }
        composable(
            route = BottomNavScreen.AddNewMileStones.route,
            arguments = listOf(
                navArgument(ADD_NEW_MILESTONE_ARGS_ID) {
                    type = NavType.IntType
                })
        ) {
            Log.d(
                "TAG",
                "BottomNavigationDrawerGraph: ${it.arguments?.getInt(ADD_NEW_MILESTONE_ARGS_ID)}"
            )
            EditMilestoneScreen(
                navHostController,
                mainViewModel,
                it.arguments?.getInt(ADD_NEW_MILESTONE_ARGS_ID)!!,
                editMilestoneViewModel,
                homeActivity = homeActivity
            )
        }
        composable(route = NavDrawerItem.IntendedParents.route) {
            IntendParentsScreen(viewModel = mainViewModel)
        }
        composable(route = NavDrawerItem.YourSurrogateMother.route) {
            YourSurrogateMother(surrogateViewModel, context, homeActivity = homeActivity)
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
            YourAccountScreen(
                navHostController = navHostController,
                yourAccountViewModel = yourAccountViewModel,
                homeActivity = homeActivity
            )
        }

    }
}