package com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph

const val ADD_NEW_MILESTONE_ARGS_TITTLE = "milestone_name"
const val ADD_NEW_MILESTONE_ARGS_DATE = "milestone_date"
const val ADD_NEW_MILESTONE_ARGS_TIME = "milestone_time"
const val ADD_NEW_MILESTONE_ARGS_ID = "milestone_id"

sealed class BottomNavScreen(
    val route: String
) {
    object MileStones : BottomNavScreen("milestones")
    object Home : BottomNavScreen("home")
    object Question : BottomNavScreen("question")
    object AddNewMileStones :
        BottomNavScreen("edit_milestone/{$ADD_NEW_MILESTONE_ARGS_ID}"){
            fun editMilestone(
                id:Int
            ):String{
                return "edit_milestone/$id"
            }
        }
}

data class MilestoneDetails(
    val tittle: String,
    val calendarIcon: Int,
    val dateTime: String,
    val locationIcon: Int,
    var show: Boolean
)