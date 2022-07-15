package com.biggestAsk.ui.homeScreen.drawerScreens.community

import com.example.biggestAsk.R

data class CommunityItems(
    val cIcon: Int,
    val cTittle: String,
    val cDescription: String,
    val btn1Text: String = "Instagram",
    val btn2Text: String = "To Forum"
)

val listCommunity = mutableListOf(
    CommunityItems(
        cIcon = R.drawable.ic_icon_community_watch,
        cTittle = "The Biggest Ask",
        cDescription = "Lorem ipsum dolor sit amet,\nconsectetur adipiscing elit.",
        btn1Text = "Instagram",
        btn2Text = "To Forum"
    ),
    CommunityItems(
        cIcon = R.drawable.ic_icon_community_star,
        cTittle = "The Biggest Ask",
        cDescription = "Lorem ipsum dolor sit amet,\nconsectetur adipiscing elit.",
        btn1Text = "Instagram",
        btn2Text = "To Forum"
    ),
    CommunityItems(
        cIcon = R.drawable.ic_icon_community_flash,
        cTittle = "The Happy Agency",
        cDescription = "Lorem ipsum dolor sit amet,\nconsectetur adipiscing elit.",
        btn1Text = "Instagram",
        btn2Text = "To Forum"
    ),
    CommunityItems(
        cIcon = R.drawable.ic_icon_community_star,
        cTittle = "The Biggest Ask #1",
        cDescription = "Lorem ipsum dolor sit amet,\nconsectetur adipiscing elit.",
        btn1Text = "Instagram",
        btn2Text = "To Forum"
    ),
    CommunityItems(
        cIcon = R.drawable.ic_icon_community_watch,
        cTittle = "The Biggest Ask #2",
        cDescription = "Lorem ipsum dolor sit amet,\nconsectetur adipiscing elit.",
        btn1Text = "Instagram",
        btn2Text = "To Forum"
    )
)
