package com.biggestAsk.data.model.response

data class GetTermsOfServiceResponse(
    val terms_of_service: List<GetTermsOfServiceResponseData>,
    val terms_of_service_date: String,
)