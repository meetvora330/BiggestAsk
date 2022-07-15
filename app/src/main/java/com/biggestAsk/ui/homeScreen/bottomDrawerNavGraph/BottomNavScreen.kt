package com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph
const val ADD_NEW_MILESTONE_ARGS_NAME="milestone_name"
sealed class BottomNavScreen(
    val route: String
) {
    object MileStones : BottomNavScreen("milestones")
    object Home : BottomNavScreen("home")
    object Question : BottomNavScreen("question")
    object AddNewMileStones : BottomNavScreen("add_new_milestone/{$ADD_NEW_MILESTONE_ARGS_NAME}")
}

data class MilestoneDetails(
    val tittle: String,
    val calendarIcon: Int,
    val dateTime: String,
    val locationIcon: Int,
    var show: Boolean
)