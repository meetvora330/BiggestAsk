package com.biggestAsk.data.model.response

data class GetNearestMilestoneResponse(
    val nearest_milestone: NearestMilestone?,
    val state: Boolean
)