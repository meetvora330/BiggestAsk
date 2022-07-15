package com.biggestAsk.data.model.request

data class IntendedParentQuestionAnsRequest(
    val user_id: Int,
    val partner_id: Int,
    val type: String
)
