package com.biggestAsk.data.model.response

data class GetContactResponseData(
    val agency_email: String,
    val agency_name: String,
    val agency_number: String,
    val created_at: String,
    val id: Int,
    val image: String,
    val title: String,
    val type: String,
    val updated_at: String,
    val user_id: Int,
)