package com.biggestAsk.data.model.response

data class EditMilestoneImageResponse(
    val created_at: String,
    val id: Int,
    val image: String,
    val milestone_user_id: Int,
    val type: String,
    val updated_at: String,
    val user_id: Int
)