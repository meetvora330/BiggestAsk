package com.biggestAsk.data.model.response

data class GetImportantQuestionResponse(
    val pregnancy_milestone: PregnancyMilestoneX,
    val question: QuestionX,
    val status: String,
    val user_name: List<String>,
)