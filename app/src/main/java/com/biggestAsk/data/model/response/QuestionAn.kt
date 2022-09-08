package com.biggestAsk.data.model.response

data class QuestionAn(
    val answer: String?,
    val category_id: Int,
    val created_at: String,
    val id: Int,
    val partner_id: Int,
    val question: String?,
    val question_id: Int,
    val type: String,
    val updated_at: String,
    val user_id: Int,
    val user_name: String? = null,
)