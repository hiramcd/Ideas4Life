package h.ideas4life.data.remote.model

import com.google.firebase.Timestamp

data class IdeaModel(
    val original: String = "",
    val improved: String = "",
    val timestamp: Timestamp? = null
)
