package com.biggestAsk.data.model.request

data class SaveNoteRequest(
    val milestone_id: Int,
    val note: String,
    val type: String,
    val share_note_with_partner: Boolean = false,
    val share_note_with_biggestask: Boolean = false,
)
