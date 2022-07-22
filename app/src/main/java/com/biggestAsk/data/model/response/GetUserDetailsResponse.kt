package com.biggestAsk.data.model.response

data class GetUserDetailsResponse(
    val address: String,
    val created_at: String,
    val date_of_birth: String,
    val email: String,
    val id: Int,
    val image1: String,
    val name: String,
    val number: String,
    val partner_id: Int,
    val partner_name: String,
    val status: String,
    val updated_at: String,
    val version: String
)