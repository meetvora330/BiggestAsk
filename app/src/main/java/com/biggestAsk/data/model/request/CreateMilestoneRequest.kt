package com.biggestAsk.data.model.request

data class CreateMilestoneRequest(
    val milestone: String,
    val user_type: String,
    val user_id: Int,
    val date: String,
    val time: String,
    val location: String,
    val longitude: String,
    val latitude: String,
)
