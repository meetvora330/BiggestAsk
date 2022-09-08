package com.biggestAsk.data.model.request

data class GetNotificationRequest(
    val type: String,
    val user_id: Int,
)