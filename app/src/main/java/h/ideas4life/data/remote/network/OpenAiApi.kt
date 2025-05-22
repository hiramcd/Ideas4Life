package h.ideas4life.data.remote.network

import h.ideas4life.data.local.dto.ChatRequest
import h.ideas4life.data.local.dto.ChatResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAiApi {

    @POST("chat/completions")
    suspend fun chatCompletion(
        @Body request: ChatRequest
    ): ChatResponse
}