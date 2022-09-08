package com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph

import com.biggestAsk.util.Constants

const val ADD_NEW_MILESTONE_ARGS_ID = Constants.MILESTONE_ID

sealed class BottomNavScreen(
    val route: String,
) {
    object MileStones : BottomNavScreen(Constants.BOTTOM_NAV_MILESTONES_SCREEN)
    object Home : BottomNavScreen(Constants.BOTTOM_NAV_HOME_SCREEN)
    object Question : BottomNavScreen(Constants.BOTTOM_NAV_QUESTION_SCREEN)
    object SurrogateParentNotAssignScreen :
        BottomNavScreen(Constants.BOTTOM_NAV_SURROGATE_PARENT_NOT_ASSIGN_SCREEN)

    object AddNewMileStones :
        BottomNavScreen("edit_milestone/{$ADD_NEW_MILESTONE_ARGS_ID}") {
        fun editMilestone(
            id: Int,
        ): String {
            return "edit_milestone/$id"
        }
    }
}