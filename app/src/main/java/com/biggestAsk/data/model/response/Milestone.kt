package com.biggestAsk.data.model.response

data class Milestone(
    val date: String? = "",
    val title: String? = null,
    val id: Int? = null,
    var show: Boolean = false,
    val type: String? = "",
)