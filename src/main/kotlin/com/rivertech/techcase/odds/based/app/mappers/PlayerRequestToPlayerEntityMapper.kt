package com.rivertech.techcase.odds.based.app.mappers

import com.rivertech.techcase.odds.based.app.entity.Player
import com.rivertech.techcase.odds.based.app.model.RegisterPlayerRequest
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface PlayerRequestToPlayerEntityMapper {
    fun toEntity(playerRequest: RegisterPlayerRequest): Player

    companion object {
        val INSTANCE: PlayerRequestToPlayerEntityMapper = Mappers.getMapper(PlayerRequestToPlayerEntityMapper::class.java)
    }
}