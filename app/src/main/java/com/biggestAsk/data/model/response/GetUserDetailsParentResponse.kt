package com.biggestAsk.data.model.response

data class GetUserDetailsParentResponse(
    val created_at: String? = null,
    val id: Int,
    val is_payment_done: Int,
    val parent_address: String? = null,
    val parent_date_of_birth: String? = null,
    val parent_email: String? = null,
    val parent_gender: String? = null,
    val parent_image1: String? = null,
    val parent_image2: String? = null,
    val parent_name: String? = null,
    val parent_number: String? = null,
    val parent_partner_gender: String? = null,
    val parent_partner_id: Int,
    val parent_partner_name: String? = null,
    val parent_password: String? = null,
    val parent_status: String? = null,
    val parent_version: String? = null,
    val updated_at: String? = null
)