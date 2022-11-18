package com.biggestAsk.ui.homeScreen.drawerScreens.settingScreens

import com.biggestAsk.util.Constants
import com.example.biggestAsk.R

/**
 * setting items & setting route defined
 */
sealed class SettingSubScreen(
    val route: String,
) {
    object AboutApp : SettingSubScreen(route = Constants.ROUTE_ABOUT_APP)
    object PrivacyPolicy : SettingSubScreen(route = Constants.ROUTE_PRIVACY_POLICY)
    object TermsOfService : SettingSubScreen(route = Constants.ROUTE_TERMS_OF_SERVICE)
    object DetailedSetting : SettingSubScreen(route = Constants.ROUTE_DETAILED_SETTINGS)
}

data class SettingsItem(
    val icon: Int,
    val title: String,
)

val listSettingsItem = mutableListOf(
    SettingsItem(
        icon = R.drawable.ic_baseline_help_outline_24,
        title = Constants.HELP
    ),
    SettingsItem(
        icon = R.drawable.ic_baseline_help_outline_24,
        title = Constants.ABOUT
    ),
    SettingsItem(
        icon = R.drawable.ic_icon_setting_item_detailed,
        title = Constants.DETAILED_SETTINGS
    ),
    SettingsItem(
        icon = R.drawable.ic_icon_setting_item_privacy_policy,
        title = Constants.PRIVACY_POLICY
    ),
    SettingsItem(
        icon = R.drawable.ic_icon_setting_item_privacy_policy,
        title = Constants.TERMS_OF_SERVICE
    )
)
