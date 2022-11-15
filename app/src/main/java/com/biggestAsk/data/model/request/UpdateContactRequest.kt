package com.biggestAsk.data.model.request

data class UpdateContactRequest(
    val id: Int,
    val title: String,
    val agency_name: String,
    val agency_email: String,
    val agency_number: String,
    val user_id: Int,
    val type: String,
)
