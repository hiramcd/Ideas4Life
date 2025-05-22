package h.ideas4life.data.local.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val role: String = "user",
    val content: String
)