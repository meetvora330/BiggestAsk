package com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph

import androidx.annotation.DrawableRes
import com.example.biggestAsk.R

sealed class BottomNavItems(
    val tittle: String,
    @DrawableRes val defaultIcon: Int,
    @DrawableRes val selectedIcon: Int,
    val navRoute: String
) {
    object Milestones : BottomNavItems(
        tittle = "Milestone",
        defaultIcon = R.drawable.ic_img_bottom_nav_icon_milestone,
        selectedIcon = R.drawable.img_milestone_bottom_bar_selected,
        BottomNavScreen.MileStones.route
    )

    object Home : BottomNavItems(
        tittle = "Home",
        defaultIcon = R.drawable.ic_img_bottom_nav_icon_home,
        selectedIcon = R.drawable.img_home_bottom_bar_selected,
        BottomNavScreen.Home.route
    )

    object Questions : BottomNavItems(
        tittle = "Questions",
        defaultIcon = R.drawable.ic_img_bottom_nav_icon_questions,
        selectedIcon = R.drawable.img_questionn_bottom_bar_selected,
        BottomNavScreen.Question.route
    )
}
