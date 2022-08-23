package com.biggestAsk.data.model.response

data class GetUserDetailsSurrogateResponse(
    val address: String? = null,
    val created_at: String? = null,
    val date_of_birth: String? = null,
    val email: String? = null,
    val id: Int,
    val image1: String? = null,
    val name: String? = null,
    val number: String? = null,
    val partner_id: Int? = null,
    val partner_name: String? = null,
    val status: String? = null,
    val updated_at: String? = null,
    val version: String? = null,
    val gender:String?=null
)