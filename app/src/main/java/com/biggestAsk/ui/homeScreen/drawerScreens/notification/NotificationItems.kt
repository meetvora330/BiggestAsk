package com.biggestAsk.ui.homeScreen.drawerScreens.notification

import com.example.biggestAsk.R

data class NotificationItems(
    val nIcon: Int,
    val nTittle: String,
    val nDays: String,
    val nDescription: String
)

val listNotification = mutableListOf(
    NotificationItems(
        nIcon = R.drawable.ic_icon_notification_1,
        nTittle = "You had the medical....",
        nDays = "1 Day ago",
        nDescription = "Your order has been shipped into\nyour address"
    ),
    NotificationItems(
        nIcon = R.drawable.ic_icon_notification_2,
        nTittle = "Time to answer a fun ....",
        nDays = "1 Day ago",
        nDescription = "Your order has been shipped\ninto your address"
    ),
    NotificationItems(
        nIcon = R.drawable.ic_icon_notification_3,
        nTittle = "Product Name ....",
        nDays = "1 Day ago",
        nDescription = "Get 10% off with coupon “STRBC\n5959"
    ),
    NotificationItems(
        nIcon = R.drawable.ic_icon_notification_1,
        nTittle = "You had the medical....",
        nDays = "1 Day ago",
        nDescription = "Get 10% off with coupon “STRBC\n5959"
    ),
    NotificationItems(
        nIcon = R.drawable.ic_icon_notification_2,
        nTittle = "Time to answer a fun ....",
        nDays = "1 Day ago",
        nDescription = "Your order has been shipped\ninto your address"
    ),
    NotificationItems(
        nIcon = R.drawable.ic_icon_notification_3,
        nTittle = "Time to answer a fun ....",
        nDays = "1 Day ago",
        nDescription = "Your order has been shipped\ninto your address"
    ),
    NotificationItems(
        nIcon = R.drawable.ic_icon_notification_2,
        nTittle = "Time to answer a fun ....",
        nDays = "1 Day ago",
        nDescription = "Your order has been shipped\ninto your address"
    ),
    NotificationItems(
        nIcon = R.drawable.ic_icon_notification_3,
        nTittle = "Time to answer a fun ....",
        nDays = "1 Day ago",
        nDescription = "Your order has been shipped\ninto your address"
    )
)

const val NOTIFICATION_DETAILS_ICON_KEY = "notification_detail_icon"
const val NOTIFICATION_DETAILS_TITTLE_KEY = "notification_detail_tittle"
const val NOTIFICATION_DETAILS_DESC_KEY = "notification_detail_description"
const val NOTIFICATION_DETAILS_DAYS_KEY = "notification_detail_days"

sealed class NotificationDetailScreenRoute(
    val route: String
) {
    object NotificationDetails :
        NotificationDetailScreenRoute(
            route = "notification_details/{$NOTIFICATION_DETAILS_ICON_KEY}/{$NOTIFICATION_DETAILS_TITTLE_KEY}/{$NOTIFICATION_DETAILS_DESC_KEY}/{$NOTIFICATION_DETAILS_DAYS_KEY}"
        ) {
        fun notificationDetails(
            icon: Int,
            tittle: String,
            desc: String,
            days: String
        ): String {
            return "notification_details/$icon/$tittle/$desc/$days"
        }
    }


}
