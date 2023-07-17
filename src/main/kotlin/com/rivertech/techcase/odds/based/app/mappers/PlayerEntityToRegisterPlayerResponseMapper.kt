package com.rivertech.techcase.odds.based.app.mappers

import com.rivertech.techcase.odds.based.app.entity.Player
import com.rivertech.techcase.odds.based.app.model.RegisterPlayerResponse
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface PlayerEntityToRegisterPlayerResponseMapper {
    fun toResponse(playerEntity: Player): RegisterPlayerResponse

    companion object {
        val INSTANCE: PlayerEntityToRegisterPlayerResponseMapper = Mappers.getMapper(PlayerEntityToRegisterPlayerResponseMapper::class.java)
    }
}