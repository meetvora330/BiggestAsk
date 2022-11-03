package com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph

import androidx.annotation.DrawableRes
import com.biggestAsk.util.Constants
import com.example.biggestAsk.R

sealed class BottomNavItems(
    val tittle: String,
    @DrawableRes val defaultIcon: Int,
    @DrawableRes val selectedIcon: Int,
    val navRoute: String,
) {
    object Milestone : BottomNavItems(
        tittle = Constants.MILESTONE,
        defaultIcon = R.drawable.ic_img_bottom_nav_icon_milestone,
        selectedIcon = R.drawable.img_milestone_bottom_bar_selected,
        BottomNavScreen.MileStones.route
    )

    object Home : BottomNavItems(
        tittle = Constants.HOME,
        defaultIcon = R.drawable.ic_img_bottom_nav_icon_home,
        selectedIcon = R.drawable.img_home_bottom_bar_selected,
        BottomNavScreen.Home.route
    )

    object Questions : BottomNavItems(
        tittle = Constants.QUESTIONS,
        defaultIcon = R.drawable.ic_img_bottom_nav_icon_questions,
        selectedIcon = R.drawable.img_questionn_bottom_bar_selected,
        BottomNavScreen.Question.route
    )
}
