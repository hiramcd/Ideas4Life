package h.ideas4life.data.local.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    @SerialName("role")
    val role: String = "user",
    @SerialName("content")
    val content: String
)