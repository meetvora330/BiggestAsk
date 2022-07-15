package com.biggestAsk.data.model.response

data class SendOtpResponse(
    val error_code: String,
    val message: String,
    val status: String
)