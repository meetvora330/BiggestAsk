package com.biggestAsk.data.model.response

data class QuestionBank(
    val created_at: String,
    val id: Int,
    val info: String,
    val info_type: String,
    val screen: Any,
    val title: Any,
    val updated_at: String
)