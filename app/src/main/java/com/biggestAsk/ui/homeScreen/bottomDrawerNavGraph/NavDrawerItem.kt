package com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph

import com.biggestAsk.util.Constants
import com.example.biggestAsk.R

sealed class NavDrawerItem(
    var route: String,
    var icon: Int,
    var tittle: String,
) {
    object YourSurrogateMother : NavDrawerItem(
        route = Constants.YOUR_SURROGATE_MOTHER,
        icon = R.drawable.ic_icon_nav_drawer_surrogate_mother,
        tittle = Constants.YOUR_SURROGATE_MOTHER_TITTLE
    )

    object IntendedParents : NavDrawerItem(
        route = Constants.INTENDED_PARENTS,
        icon = R.drawable.ic_icon_nav_drawer_surrogate_mother,
        tittle = Constants.INTENDED_PARENTS_TITTLE
    )

    object Community :
        NavDrawerItem(
            route = Constants.COMMUNITY,
            icon = R.drawable.ic_icon_nav_drawer_community,
            tittle = Constants.COMMUNITY_TITTLE
        )

    object ContactYourProviders : NavDrawerItem(
        route = Constants.CONTACT_YOUR_PROVIDER,
        icon = R.drawable.ic_icon_nav_drawer_contact_your_provider,
        tittle = Constants.CONTACT_YOUR_PROVIDER_TITTLE
    )

    object Notifications : NavDrawerItem(
        route = Constants.NOTIFICATIONS,
        icon = R.drawable.ic_icon_nav_drawer_notification,
        tittle = Constants.NOTIFICATIONS_TITTLE
    )

    object Settings : NavDrawerItem(
        route = Constants.SETTINGS,
        icon = R.drawable.ic_icon_nav_drawer_settings,
        tittle = Constants.SETTINGS_TITTLE
    )

}

sealed class MyAccount(
    val route: String,
) {
    object MyAccountScreen : MyAccount(Constants.MY_ACCOUNT)
}

