package h.ideas4life.data.local.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<ChatMessage>
)