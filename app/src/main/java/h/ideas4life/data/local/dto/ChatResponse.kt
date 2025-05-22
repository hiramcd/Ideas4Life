package h.ideas4life.data.local.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val choices: List<Choice>
)

@Serializable
data class Choice(
    val index: Int,
    val message: ChatMessage,
    @SerialName("finish_reason") val finishReason: String
)