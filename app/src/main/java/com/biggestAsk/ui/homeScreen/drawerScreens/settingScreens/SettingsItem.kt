package com.biggestAsk.ui.homeScreen.drawerScreens.settingScreens

import com.example.biggestAsk.R

sealed class SettingSubScreen(
    val route: String
) {
    object AboutApp : SettingSubScreen(route = "about_app")
    object PrivacyPolicy : SettingSubScreen(route = "privacy_policy")
    object TermsOfService : SettingSubScreen(route = "terms_of_service")
    object DetailedSetting : SettingSubScreen(route = "detailed_setting")
}

data class SettingsItem(
    val icon: Int,
    val title: String
)

val listSettingsItem = mutableListOf(
    SettingsItem(
        icon = R.drawable.ic_baseline_help_outline_24,
        title = "Help"
    ),
    SettingsItem(
        icon = R.drawable.ic_baseline_help_outline_24,
        title = "About"
    ),
    SettingsItem(
        icon = R.drawable.ic_icon_setting_item_detailed,
        title = "Detailed Settings"
    ),
    SettingsItem(
        icon = R.drawable.ic_icon_setting_item_privacy_policy,
        title = "Privacy Policy"
    ),
    SettingsItem(
        icon = R.drawable.ic_icon_setting_item_privacy_policy,
        title = "Terms of Service"
    )
)
