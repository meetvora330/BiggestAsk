package com.biggestAsk.data.model.response

data class IndendedUser(
    val address: String,
    val date_of_birth: String,
    val email: String,
    val image:String?=null,
    val image1: String? = null,
    val image2: String? = null,
    val name: String,
    val number: String,
    val partner_name:String,
    val partner_date_of_birth:String,
    val partner_address:String,
    val partner_number:String,
    val partner_email:String
)