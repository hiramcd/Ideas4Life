package h.ideas4life.data.remote.repository

import h.ideas4life.data.remote.model.IdeaModel
import h.ideas4life.data.remote.services.FirestoreService

class IdeaRepository(
    private val firestoreService: FirestoreService
) {
    fun getIdeas() = firestoreService.getIdeasFlow()
    fun saveIdea(
        idea: IdeaModel,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestoreService.saveIdea(idea, onSuccess, onFailure)
    }
}