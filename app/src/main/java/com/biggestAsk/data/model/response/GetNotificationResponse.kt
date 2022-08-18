package com.biggestAsk.data.model.response

data class GetNotificationResponse(
    val `data`: List<GetNotificationResponseData>,
    val days: List<Int>
)