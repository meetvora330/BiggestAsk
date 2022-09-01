package com.biggestAsk.data.model.response

data class GetPrivacyPolicyResponse(
    val privacy_policy: List<GetPrivacyPolicyResponseData>,
    val privacy_policy_date: String
)