package com.biggestAsk.ui.homeScreen.drawerScreens.notification

const val NOTIFICATION_DETAILS_ICON_KEY = "notification_detail_icon"
const val NOTIFICATION_DETAILS_TITTLE_KEY = "notification_detail_tittle"
const val NOTIFICATION_DETAILS_DESC_KEY = "notification_detail_description"
const val NOTIFICATION_DETAILS_DAYS_KEY = "notification_detail_days"

sealed class NotificationDetailScreenRoute(
    val route: String,
) {
    object NotificationDetails :
        NotificationDetailScreenRoute(
            route = "notification_details/{$NOTIFICATION_DETAILS_ICON_KEY}/{$NOTIFICATION_DETAILS_TITTLE_KEY}/{$NOTIFICATION_DETAILS_DESC_KEY}/{$NOTIFICATION_DETAILS_DAYS_KEY}"
        ) {
        fun notificationDetails(
            icon: Int,
            tittle: String,
            desc: String,
            days: Int,
        ): String {
            return "notification_details/$icon/$tittle/$desc/$days"
        }
    }
}
