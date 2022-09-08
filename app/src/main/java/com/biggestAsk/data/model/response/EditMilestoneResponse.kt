package com.biggestAsk.data.model.response

data class EditMilestoneResponse(
    val milestone: List<MilestoneData>,
    val milestone_image: List<EditMilestoneImageResponse>,
)