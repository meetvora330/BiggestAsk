package com.biggestAsk.data.model.response

data class UpdateImageResponse(
    val error_code: String,
    val message: String,
    val status: String,
    val image_url: String
)