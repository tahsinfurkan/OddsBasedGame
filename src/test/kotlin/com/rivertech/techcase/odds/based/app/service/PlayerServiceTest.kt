package com.rivertech.techcase.odds.based.app.service

import com.rivertech.techcase.odds.based.app.entity.Player
import com.rivertech.techcase.odds.based.app.exceptions.PlayerNotFoundException
import com.rivertech.techcase.odds.based.app.mappers.PlayerRequestToPlayerEntityMapper
import com.rivertech.techcase.odds.based.app.model.RegisterPlayerRequest
import com.rivertech.techcase.odds.based.app.repository.PlayerRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.*

class PlayerServiceTest {

    private val playerRepository: PlayerRepository = mock(PlayerRepository::class.java)
    private val playerService = PlayerService(playerRepository)

    @Test
    fun testRegisterPlayer() {
        // Prepare test data
        val registerPlayerRequest = RegisterPlayerRequest("tfk", "tahsinfurkan", "keles")
        val playerEntity = PlayerRequestToPlayerEntityMapper.INSTANCE.toEntity(registerPlayerRequest)
        val savedPlayerEntity = Player("tfk", "tahsinfurkan", "keles")

        // Mock the playerRepository.save() method
        `when`(playerRepository.save(any(Player::class.java))).thenReturn(savedPlayerEntity)

        // Call the registerPlayer() method
        val result = playerService.registerPlayer(registerPlayerRequest).block()

        // Verify the result
        assertEquals(playerEntity.username, result?.username)
        assertEquals(playerEntity.name, result?.name)
        assertEquals(playerEntity.surname, result?.surname)
    }

    @Test
    fun testGetPlayerById_PlayerExists() {
        // Prepare test data
        val playerEntity = Player("tfk", "tahsinfurkan", "keles")
        val playerId = playerEntity.id

        // Mock the playerRepository.findById() method
        `when`(playerRepository.findById(playerId)).thenReturn(Optional.of(playerEntity))

        // Call the getPlayerById() method
        val result = playerService.getPlayerById(playerId).block()

        // Verify the result
        assertEquals(playerEntity.id, result?.id)
        assertEquals(playerEntity.username, result?.username)
        assertEquals(playerEntity.name, result?.name)
        assertEquals(playerEntity.surname, result?.surname)
    }

    @Test
    fun testGetPlayerById_PlayerNotFound() {
        // Prepare test data
        val playerId = UUID.randomUUID()

        // Mock the playerRepository.findById() method
        `when`(playerRepository.findById(playerId)).thenReturn(Optional.empty())

        // Call the getPlayerById() method and assert an exception is thrown
        assertThrows(PlayerNotFoundException::class.java) {
            playerService.getPlayerById(playerId).block()
        }
    }
}