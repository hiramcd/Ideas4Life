package h.ideas4life.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import h.ideas4life.BuildConfig
import h.ideas4life.data.local.dto.ChatMessage
import h.ideas4life.data.local.dto.ChatRequest
import h.ideas4life.data.remote.model.IdeaModel
import h.ideas4life.data.remote.network.OpenAiClient
import h.ideas4life.data.remote.repository.IdeaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class IdeaViewModel @Inject constructor(
    private val repository: IdeaRepository
) : ViewModel() {
    private val PROMT = BuildConfig.PROMPT

    private val _ideas = MutableStateFlow<List<IdeaModel>>(emptyList())
    val ideas: StateFlow<List<IdeaModel>> = _ideas

    private val _isAddIdeaVisible = MutableStateFlow(false)
    val isAddIdeaVisible: StateFlow<Boolean> = _isAddIdeaVisible

    private val _aiResponse = MutableStateFlow("")
    val aiResponse: StateFlow<String> = _aiResponse

    init {
        viewModelScope.launch {
            repository.getIdeas().collect{
                _ideas.value = it
            }
        }
    }

    fun askAi(idea: String) {
        viewModelScope.launch {
            try {
                val  prompt = PROMT + idea
                val request = ChatRequest(
                    model = "gpt-3.5-turbo",
                    messages = listOf(ChatMessage(role = "user", content = prompt))
                )
                val response = OpenAiClient.api.chatCompletion(request)
                Log.e("OPENAI", "HttpException: $response")
                val reply = response.choices.firstOrNull()?.message?.content ?: "Sin respuesta"
                _aiResponse.value = reply

            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("OPENAI", "HttpException: ${e.code()} - $errorBody")
                _aiResponse.value = "Error ${e.code()}: $errorBody"
            } catch (e: Exception) {
                Log.e("OPENAI", "Exception inesperada", e)
                _aiResponse.value = "Excepción: ${e.localizedMessage}"
            }
        }
    }

    fun toggleAddIdea() {
        _isAddIdeaVisible.value = !_isAddIdeaVisible.value
    }

    fun saveIdea(text: String) {
        val idea = IdeaModel(
            original = text,
            improved = ""
        )
        repository.saveIdea(
            idea,
            onSuccess = {
                Log.d("FIREBASE_WRITE", "Idea Guardada Exitosamente")
            },
            onFailure = { e ->
                Log.e("FIREBASE_WRITE", "Error al guardar idea", e)
            }
        )

    }


}
