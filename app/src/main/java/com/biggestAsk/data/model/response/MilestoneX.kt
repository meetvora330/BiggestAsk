package com.biggestAsk.data.model.response

data class MilestoneX(
    val created_at: String?="",
    val date: String?="",
    val date_status: Int?=0,
    val id: Int?=0,
    val latitude: String?="",
    val location: String?="",
    val longitude: String?="",
    val milestone_id: Int?=0,
    val p_note_biggest: Int?=0,
    val p_note_status: Int?=0,
    val parent_id: Int?=0,
    val parent_note: Any?=0,
    val s_note_biggest: Int?=0,
    val s_note_status: Int?=0,
    val status: String?="",
    val surrogate_id: Int?=0,
    val surrogate_note: Any?=0,
    val time: String?="",
    val title: String?="",
    val type: String?="",
    val updated_at: String?=""
)