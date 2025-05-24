package h.ideas4life.data.remote.services

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import h.ideas4life.data.local.mapper.toWriteDto
import h.ideas4life.data.remote.model.IdeaModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class IdeaServiceImpl @Inject constructor() : IdeaService {
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun getIdeasFlow(): Flow<List<IdeaModel>> = callbackFlow {
        val subscription = db.collection("ideas")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val ideas = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(IdeaModel::class.java)
                } ?: emptyList()

                trySend(ideas)
            }

        awaitClose { subscription.remove() }
    }

    override fun saveIdea(idea: IdeaModel, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val dto = idea.toWriteDto()
        db.collection("ideas")
            .add(dto)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }
}