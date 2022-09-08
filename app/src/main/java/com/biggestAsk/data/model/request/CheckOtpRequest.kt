package com.biggestAsk.data.model.request

data class CheckOtpRequest(
    val otp: String,
    val email: String,
)
