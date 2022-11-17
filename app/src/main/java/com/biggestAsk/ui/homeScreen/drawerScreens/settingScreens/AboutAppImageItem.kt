package com.biggestAsk.ui.homeScreen.drawerScreens.settingScreens

import com.example.biggestAsk.R

/**
 * about app image item
 */

data class AboutAppImageItem(
    val img: Int,
)

val listAboutAppImageItem = mutableListOf(
    AboutAppImageItem(img = R.drawable.ic_seting_about_app_fb_icon),
    AboutAppImageItem(img = R.drawable.ic_seting_about_app_insta_icon),
    AboutAppImageItem(img = R.drawable.ic_seting_about_app_twitter_icon),
    AboutAppImageItem(img = R.drawable.ic_seting_about_app_browser_icon)
)
