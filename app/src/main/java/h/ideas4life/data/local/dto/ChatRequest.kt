package h.ideas4life.data.local.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    @SerialName("model")
    val model: String = "gpt-3.5-turbo",
    @SerialName("messages")
    val messages: List<ChatMessage>
)