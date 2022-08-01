package com.biggestAsk.data.model.response

data class GetPregnancyMilestoneResponse(
    val pregnancy_milestone: PregnancyMilestone,
    val question: Question,
    val status: String
)