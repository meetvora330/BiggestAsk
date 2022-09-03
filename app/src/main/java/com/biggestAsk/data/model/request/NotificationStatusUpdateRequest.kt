package com.biggestAsk.data.model.request

data class NotificationStatusUpdateRequest(
    val type: String,
    val user_id: Int,
    val status: String
)
