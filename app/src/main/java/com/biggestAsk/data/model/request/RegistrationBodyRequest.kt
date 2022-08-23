package com.biggestAsk.data.model.request

data class RegistrationBodyRequest(
    val type: String,
    val name: String,
    val email: String,
    val password: String,
    val gender:String
)
