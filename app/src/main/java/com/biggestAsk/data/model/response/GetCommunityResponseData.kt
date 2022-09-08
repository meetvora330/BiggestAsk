package com.biggestAsk.data.model.response

data class GetCommunityResponseData(
    val created_at: String,
    val description: String,
    val forum_link: String,
    val id: Int,
    val image: String,
    val insta_link: String,
    val title: String,
    val type: String,
    val updated_at: String,
    val user_id: Int,
)