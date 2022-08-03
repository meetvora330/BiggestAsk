package com.biggestAsk.data.model.response

data class MilestoneX(
    val date: String,
    val id: Int,
    val location: String,
    val milestone_id: Int,
    val parent_id: Int,
    val parent_note: Any,
    val share_note_with_biggestask_status: Int,
    val share_note_with_partner_status: Int,
    val surrogate_id: Int,
    val surrogate_note: Any,
    val time: String,
    val title: String,
    val type: String
)