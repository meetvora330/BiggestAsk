package com.biggestAsk.data.model.response

data class GetHomeScreenQuestionResponse(
    val `data`: Data,
    val status: String,
    val status_code: String,
    val user_name: List<String>,
)