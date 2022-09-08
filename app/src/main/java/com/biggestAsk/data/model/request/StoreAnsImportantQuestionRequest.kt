package com.biggestAsk.data.model.request

data class StoreAnsImportantQuestionRequest(
    val question_id: Int,
    val answer: String,
    val user_name: String,
)
