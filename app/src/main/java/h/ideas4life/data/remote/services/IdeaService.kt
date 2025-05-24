package h.ideas4life.data.remote.services

import com.google.rpc.QuotaFailure
import h.ideas4life.data.remote.model.IdeaModel
import kotlinx.coroutines.flow.Flow

interface IdeaService {
    fun getIdeasFlow(): Flow<List<IdeaModel>>
    fun saveIdea(idea: IdeaModel, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
}