package com.biggestAsk.data.model.request

data class EditMilestoneRequest(
    val type: String,
    val user_id: Int,
    val milestone_id: Int,
    val partner_id: Int,
)
