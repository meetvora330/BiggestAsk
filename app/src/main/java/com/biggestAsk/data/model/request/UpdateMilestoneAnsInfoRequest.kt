package com.biggestAsk.data.model.request

data class UpdateMilestoneAnsInfoRequest(
    val title: String,
    val time: String,
    val location: String,
    val date: String,
    val longitude: String,
    val latitude: String,
    val user_id: Int,
    val type: String,
    val milestone_id: Int,
)
