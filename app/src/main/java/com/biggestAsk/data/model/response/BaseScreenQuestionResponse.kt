package com.biggestAsk.data.model.response

data class BaseScreenQuestionResponse(
    val category_id: Int,
    val created_at: String,
    val id: Int,
    val question: String,
    val updated_at: String,
    val answer: String
)