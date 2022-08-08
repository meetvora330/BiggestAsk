package com.biggestAsk.data.model.response

data class GetNearestMilestoneResponse(
    val date: String,
    val time: String,
    val title: String,
    val milestone_image: String
)