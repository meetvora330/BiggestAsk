package com.biggestAsk.data.model.response

import android.net.Uri

data class EditMilestoneImageResponse(
    val created_at: String,
    val id: Int,
    val image: String,
    val milestone_user_id: Int,
    val type: String,
    val updated_at: String,
    val user_id: Int,
    var uriPath: String? = null,
    var imageUri: Uri? = null,
    var is_need_to_upload: Boolean = false
)