package com.biggestAsk.data.model.response

data class MilestoneX(
    val created_at: String,
    val date: String,
    val date_status: Int,
    val id: Int,
    val latitude: Any,
    val location: Any,
    val longitude: Any,
    val milestone_id: Int,
    val parent_id: Int,
    val parent_note: Any,
    val parent_share_note_with_biggestask_status: Int,
    val parent_share_note_with_partner_status: Int,
    val status: String,
    val surrogate_id: Int,
    val surrogate_note: Any,
    val surrogate_share_note_with_biggestask_status: Int,
    val surrogate_share_note_with_partner_status: Int,
    val time: String,
    val title: String,
    val type: String,
    val updated_at: String
)