package h.ideas4life.data.remote.repository

import h.ideas4life.data.remote.model.IdeaModel
import h.ideas4life.data.remote.services.IdeaService
import h.ideas4life.data.remote.services.IdeaServiceImpl
import javax.inject.Inject

class IdeaRepository @Inject constructor(
    private val ideaService: IdeaService
) {
    fun getIdeas() = ideaService.getIdeasFlow()
    fun saveIdea(
        idea: IdeaModel,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        ideaService.saveIdea(idea, onSuccess, onFailure)
    }
}