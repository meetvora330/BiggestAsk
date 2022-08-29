package com.biggestAsk.data.model.response

data class LoginBodyResponse(
    val is_payment_done: Boolean,
    val is_question_answered: Boolean,
    val message: String,
    val partner_id: Int,
    val status: String,
    val type: String,
    val user_id: Int,
    val user_name: String,
    val surrogate: String,
    val parent1: String,
    val parent2: String
)