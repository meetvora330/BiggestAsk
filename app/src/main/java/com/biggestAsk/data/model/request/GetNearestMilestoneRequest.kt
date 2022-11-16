package com.biggestAsk.data.model.request

data class GetNearestMilestoneRequest(
    val user_id:Int,
    val type:String,
    val partner_id:Int
)