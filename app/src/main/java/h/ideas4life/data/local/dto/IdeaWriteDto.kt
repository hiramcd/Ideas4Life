package h.ideas4life.data.local.dto

import com.google.firebase.firestore.FieldValue

data class IdeaWriteDto(
    val original: String,
    val improved: String,
    val timestamp: Any = FieldValue.serverTimestamp()
)