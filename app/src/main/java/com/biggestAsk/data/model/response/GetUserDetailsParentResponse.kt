package com.biggestAsk.data.model.response

data class GetUserDetailsParentResponse(
    val created_at: String,
    val id: Int,
    val is_payment_done: Int,
    val parent_address: String?=null,
    val parent_date_of_birth: String?=null,
    val parent_email: String?=null,
    val parent_gender: String,
    val parent_image1: String?=null,
    val parent_image2: String?=null,
    val parent_name: String?=null,
    val parent_number: String?=null,
    val parent_partner_address: String?=null,
    val parent_partner_dob: String?=null,
    val parent_partner_gender: String,
    val parent_partner_id: Int,
    val parent_partner_name: String?=null,
    val parent_partner_phone: Int?=null,
    val parent_password: String,
    val parent_status: String,
    val parent_version: Any,
    val updated_at: String
)