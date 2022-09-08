package com.biggestAsk.data.model.response

data class ResetMilestoneResponse(
    val image: String,
    val message: String,
    val partner_id: Int,
    val pregnancy_milestone_status: String,
    val status: String,
)