package com.biggestAsk.data.model.request

data class SaveNoteRequest(
    val milestone_id: Int,
    val note: String,
    val type: String
)
