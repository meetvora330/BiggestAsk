package com.biggestAsk.ui.homeScreen.drawerScreens.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * notification details screen
 */
@Composable
fun NotificationDetails(
    notificationDetailsIcon: Int?,
    notificationDetailsTittle: String?,
    notificationDetailsDescription: String?,
    notificationDetailsDays: String?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 41.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = notificationDetailsIcon!!),
                contentDescription = ""
            )
            Text(
                text = notificationDetailsTittle!!,
                modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.body1,
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                lineHeight = 24.sp,
                color = Color.Black
            )
        }
        Text(
            modifier = Modifier.padding(top = 7.dp),
            text = notificationDetailsDescription!!,
            style = MaterialTheme.typography.body1,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = Color(0xFF8995A3)
        )
        Text(
            modifier = Modifier
                .wrapContentWidth()
                .padding(top = 20.dp),
            text = "${notificationDetailsDays!!} days ago",
            style = MaterialTheme.typography.body1,
            fontSize = 14.sp,
            fontWeight = FontWeight.W400,
            color = Color(0xFF9F9D9B)
        )
    }
}