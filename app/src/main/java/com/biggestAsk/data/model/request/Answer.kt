package com.biggestAsk.data.model.request

data class Answer(
    val answer: String,
    val question_id: Int,
    val user_name:String
)