package com.rivertech.techcase.odds.based.app.service

import com.rivertech.techcase.odds.based.app.entity.Player
import com.rivertech.techcase.odds.based.app.exceptions.PlayerNotFoundException
import com.rivertech.techcase.odds.based.app.mappers.PlayerEntityToRegisterPlayerResponseMapper
import com.rivertech.techcase.odds.based.app.mappers.PlayerRequestToPlayerEntityMapper
import com.rivertech.techcase.odds.based.app.model.RegisterPlayerRequest
import com.rivertech.techcase.odds.based.app.model.RegisterPlayerResponse
import com.rivertech.techcase.odds.based.app.repository.PlayerRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class PlayerService(private val playerRepository: PlayerRepository) {

    fun registerPlayer(registerPlayerRequest: RegisterPlayerRequest): Mono<RegisterPlayerResponse> {
        val playerEntity = PlayerRequestToPlayerEntityMapper.INSTANCE.toEntity(registerPlayerRequest)
        val savedPlayerEntity = playerRepository.save(playerEntity)

        return Mono.fromCallable {
            PlayerEntityToRegisterPlayerResponseMapper.INSTANCE.toResponse(savedPlayerEntity)
        }
    }

    fun getPlayerById(playerId: UUID): Mono<Player> {
        return Mono.just(playerRepository.findById(playerId)
                .orElseThrow { PlayerNotFoundException("Player not found with ID: $playerId") })
    }
}