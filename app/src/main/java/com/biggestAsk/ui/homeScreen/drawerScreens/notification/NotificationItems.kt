package com.biggestAsk.ui.homeScreen.drawerScreens.notification

const val NOTIFICATION_DETAILS_ICON_KEY = "notification_detail_icon"
const val NOTIFICATION_DETAILS_TITTLE_KEY = "notification_detail_tittle"
const val NOTIFICATION_DETAILS_DESC_KEY = "notification_detail_description"
const val NOTIFICATION_DETAILS_DAYS_KEY = "notification_detail_days"
const val NOTIFICATION_DETAILS = "notification_details"

/**
 * sealed class with notification route defined
 */

sealed class NotificationDetailScreenRoute(
    val route: String,
) {
    object NotificationDetails :
        NotificationDetailScreenRoute(
            route = "$NOTIFICATION_DETAILS/{$NOTIFICATION_DETAILS_ICON_KEY}/{$NOTIFICATION_DETAILS_TITTLE_KEY}/{$NOTIFICATION_DETAILS_DESC_KEY}/{$NOTIFICATION_DETAILS_DAYS_KEY}"
        ) {
        fun notificationDetails(
            icon: Int,
            tittle: String,
            desc: String,
            days: Int,
        ): String {
            return "$NOTIFICATION_DETAILS/$icon/$tittle/$desc/$days"
        }
    }
}
