package com.biggestAsk.data.model.response

data class GetUserDetailsParentResponse(
    val created_at: String,
    val id: Int,
    val is_payment_done: Int,
    val parent_address: String,
    val parent_date_of_birth: String,
    val parent_email: String,
    val parent_gender: String,
    val parent_image1: String,
    val parent_image2: String,
    val parent_name: String,
    val parent_number: String,
    val parent_partner_address: String,
    val parent_partner_dob: String,
    val parent_partner_gender: String,
    val parent_partner_id: Int,
    val parent_partner_name: String,
    val parent_partner_phone: Int,
    val parent_password: String,
    val parent_status: String,
    val parent_version: Any,
    val updated_at: String
)