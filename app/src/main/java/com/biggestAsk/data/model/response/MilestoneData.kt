package com.biggestAsk.data.model.response

data class MilestoneData(
    val date: String,
    val id: Int,
    val location: String,
    val milestone_id: Int,
    val milestone_image: String,
    val parent_id: Int,
    val parent_note: String,
    val share_note_with_biggestask_status: Int,
    val share_note_with_partner_status: Int,
    val surrogate_id: Int,
    val surrogate_note: String,
    val surrogate_share_note_with_biggestask_status: Int,
    val time: String,
    val title: String,
    val type: String
)