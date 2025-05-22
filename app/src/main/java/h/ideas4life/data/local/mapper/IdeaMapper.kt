package h.ideas4life.data.local.mapper

import h.ideas4life.data.local.dto.IdeaWriteDto
import h.ideas4life.data.remote.model.IdeaModel

fun IdeaModel.toWriteDto(): IdeaWriteDto{
        return IdeaWriteDto(
            original = this.original,
            improved = this.improved
        )
    }