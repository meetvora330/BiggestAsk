package com.biggestAsk.data.model.response

data class GetIntendedProfileResponse(
    val days: List<Int>,
    val indended_user: IndendedUser,
    val question_ans: List<QuestionAn>,
    val status: String,
)