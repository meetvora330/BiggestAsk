package com.biggestAsk.ui.homeScreen.drawerScreens.settingScreens

data class DetailedSettingsItem(
    val startTittle: String,
    val endTittle: String,
    val item: String,
)

val listDetailedSettingsItem = mutableListOf(
    DetailedSettingsItem(
        startTittle = "Language",
        endTittle = "Change",
        item = "English",
    ),
    DetailedSettingsItem(
        startTittle = "Location",
        endTittle = "Edit",
        item = "London,UK",
    )
)