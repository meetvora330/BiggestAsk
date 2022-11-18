package com.biggestAsk.data.model.request

data class AskSurrogateRequest(
    val user_id: Int,
    val title: String,
    val milestone_id: Int,
)
