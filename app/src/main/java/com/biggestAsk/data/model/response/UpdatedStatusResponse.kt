package com.biggestAsk.data.model.response

data class UpdatedStatusResponse(
    val status: String,
    val partner_id: Int,
    val image: String? = "",
    val pregnancy_milestone_status: String? = "",
    val user_name: String,
)