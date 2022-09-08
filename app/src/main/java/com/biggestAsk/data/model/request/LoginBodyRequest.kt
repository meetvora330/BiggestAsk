package com.biggestAsk.data.model.request

data class LoginBodyRequest(
    val email: String,
    val password: String,
    val fcm_token: String,
)
