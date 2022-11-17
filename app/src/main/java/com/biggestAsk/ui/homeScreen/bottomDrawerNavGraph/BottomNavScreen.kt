package com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph

import com.biggestAsk.util.Constants
import com.biggestAsk.util.Constants.SELECTED_MILESTONE_INDEX

const val ADD_NEW_MILESTONE_ARGS_ID = Constants.MILESTONE_ID
const val editMilestone = "edit_milestone"

/**
 * used for bottom navigation route
 */
sealed class BottomNavScreen(
    val route: String,
) {
    object MileStones : BottomNavScreen(Constants.BOTTOM_NAV_MILESTONES_SCREEN)
    object Home : BottomNavScreen(Constants.BOTTOM_NAV_HOME_SCREEN)
    object Question : BottomNavScreen(Constants.BOTTOM_NAV_QUESTION_SCREEN)
    object SurrogateParentNotAssignScreen :
        BottomNavScreen(Constants.BOTTOM_NAV_SURROGATE_PARENT_NOT_ASSIGN_SCREEN)

    object AddNewMileStones :
        BottomNavScreen("$editMilestone/{$ADD_NEW_MILESTONE_ARGS_ID}/{$SELECTED_MILESTONE_INDEX}") {
        fun editMilestone(
            id: Int,
            index:Int
        ): String {
            return "$editMilestone/$id/$index"
        }
    }
}