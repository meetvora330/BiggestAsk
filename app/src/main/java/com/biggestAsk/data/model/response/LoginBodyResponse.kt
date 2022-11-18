package com.biggestAsk.data.model.response

data class LoginBodyResponse(
    val is_payment_done: Boolean,
    val is_question_answered: Boolean,
    val message: String? = null,
    val partner_id: Int? = null,
    val status: String? = null,
    val type: String? = null,
    val user_id: Int? = null,
    val user_name: String? = null,
    val image: String? = null,
    val pregnancy_milestone_status: String? = null,
)