package com.biggestAsk.data.model.response

data class GetPregnancyMilestoneResponse(
    val created_at: String,
    val description: String,
    val id: Int,
    val image: String,
    val title: String,
    val updated_at: String,
    val week: Int
)