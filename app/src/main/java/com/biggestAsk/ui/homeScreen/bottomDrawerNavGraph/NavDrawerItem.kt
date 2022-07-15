package com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph

import com.example.biggestAsk.R

sealed class NavDrawerItem(
    var route: String,
    var icon: Int,
    var tittle: String
) {
    object YourSurrogateMother : NavDrawerItem(
        route = "your_surrogate_mother",
        icon = R.drawable.ic_icon_nav_drawer_surrogate_mother,
        tittle = "Your Surrogate Mother"
    )

    object IntendedParents : NavDrawerItem(
        route = "intended_parents",
        icon = R.drawable.ic_icon_nav_drawer_surrogate_mother,
        tittle = "Intended Parents"
    )

    object Community :
        NavDrawerItem(
            route = "community",
            icon = R.drawable.ic_icon_nav_drawer_community,
            tittle = "Community"
        )

    object ContactYourProviders : NavDrawerItem(
        route = "contact_your_providers",
        icon = R.drawable.ic_icon_nav_drawer_contact_your_provider,
        tittle = "Contact Your Provider"
    )

    object Notifications : NavDrawerItem(
        route = "notifications",
        icon = R.drawable.ic_icon_nav_drawer_notification,
        tittle = "Notifications"
    )

    object Settings : NavDrawerItem(
        route = "settings",
        icon = R.drawable.ic_icon_nav_drawer_settings,
        tittle = "Settings"
    )
}

sealed class MyAccount(
    val route: String
) {
    object MyAccountScreen : MyAccount("my_account")
}

