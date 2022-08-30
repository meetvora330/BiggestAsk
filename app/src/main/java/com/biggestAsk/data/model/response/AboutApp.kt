package com.biggestAsk.data.model.response

data class AboutApp(
    val created_at: String,
    val id: Int,
    val info: String,
    val info_type: String,
    val screen: Any,
    val title: String,
    val updated_at: String
)