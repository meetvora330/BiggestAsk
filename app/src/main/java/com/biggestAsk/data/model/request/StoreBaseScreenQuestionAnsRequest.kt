package com.biggestAsk.data.model.request

data class StoreBaseScreenQuestionAnsRequest(
    val answer: List<Answer>,
    val category_id: Int,
    val partner_id: String,
    val type: String,
    val user_id: Int,
    val user_name: String,
)