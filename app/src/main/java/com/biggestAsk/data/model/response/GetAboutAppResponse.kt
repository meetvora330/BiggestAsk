package com.biggestAsk.data.model.response

data class GetAboutAppResponse(
    val about_app: List<GetAboutAppResponseData>,
    val about_app_date: String,
)