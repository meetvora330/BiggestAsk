package com.biggestAsk.data.model.response

data class GetNotificationResponseData(
    val date: String,
    val id: Int,
    val notification: String,
    val title: String,
)