package com.biggestAsk.data.model.request

import com.biggestAsk.data.model.response.Answer

data class StoreQuestionAnsRequest(
    val answer: List<Answer>,
    val category_id: Int,
    val partner_id: Int,
    val type: String,
    val user_id: Int
)