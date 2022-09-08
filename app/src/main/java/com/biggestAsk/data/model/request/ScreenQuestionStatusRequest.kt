package com.biggestAsk.data.model.request

data class ScreenQuestionStatusRequest(
    val type: String,
    val user_id: Int,
    val question_type: String,
)
