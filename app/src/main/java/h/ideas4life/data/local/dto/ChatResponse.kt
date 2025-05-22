package h.ideas4life.data.local.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val choices: List<Choice>
)

@Serializable
data class Choice(
    val message: ChatMessage
)