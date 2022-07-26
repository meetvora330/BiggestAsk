package com.biggestAsk.data.model.request

/**
 * Created by Abhin.
 */
data class ResetMilestoneRequest(
    var milestone_id: List<Int>,
    var type: String,
    var user_id: Int
)