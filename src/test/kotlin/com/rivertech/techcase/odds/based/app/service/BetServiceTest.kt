package com.rivertech.techcase.odds.based.app.service

import com.rivertech.techcase.odds.based.app.common.TransactionType
import com.rivertech.techcase.odds.based.app.entity.Bet
import com.rivertech.techcase.odds.based.app.entity.Player
import com.rivertech.techcase.odds.based.app.exceptions.NotEnoughCreditException
import com.rivertech.techcase.odds.based.app.mappers.BetEntityToPlaceBetResponseMapper
import com.rivertech.techcase.odds.based.app.mappers.BetTransactionRequestToResponseMapper
import com.rivertech.techcase.odds.based.app.repository.BetRepository
import com.rivertech.techcase.odds.based.app.repository.PlayerRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.util.*

class BetServiceTest {

    private val betRepository: BetRepository = mock(BetRepository::class.java)
    private val playerRepository: PlayerRepository = mock(PlayerRepository::class.java)
    private val playerService: PlayerService = mock(PlayerService::class.java)
    private val walletTransactionService: WalletTransactionService = mock(WalletTransactionService::class.java)

    private val betService = BetService(betRepository, playerRepository, playerService, walletTransactionService)

    @Test
    fun testPlaceBet_Success() {
        val playerId = UUID.randomUUID()
        val betAmount = BigDecimal("100")
        val betNumber = 3
        val player = Player("tfk", "tahsinfurkan", "keles")
        val betEntity = Bet(player, betAmount, betNumber)
        doNothing().`when`(walletTransactionService).updateWallet(player, betAmount, betEntity, TransactionType.DEBIT)
        val generatedNumber = 3
        val betResult = calculateWinnings(betEntity, generatedNumber)
        val expectedResponse = BetEntityToPlaceBetResponseMapper.INSTANCE.toResponse(betEntity, generatedNumber, TransactionType.CREDIT, betResult, player.walletBalance.add(betResult))

        `when`(playerService.getPlayerById(playerId)).thenReturn(Mono.just(player))
        `when`(betRepository.save(any(Bet::class.java))).thenReturn(betEntity)
        doNothing().`when`(walletTransactionService).updateWallet(player, betResult, betEntity, TransactionType.CREDIT)

        val result = betService.placeBet(playerId, betAmount, betNumber).block()
        assertNotNull(expectedResponse)
        assertNotNull(result)
    }

    private fun calculateWinnings(bet: Bet, generatedNumber: Int): BigDecimal {
        val betNumber = bet.betNumber
        return when {
            betNumber == generatedNumber -> bet.betAmount.multiply(BigDecimal.TEN)
            Math.abs(betNumber - generatedNumber) == 1 -> bet.betAmount.multiply(BigDecimal.valueOf(5))
            Math.abs(betNumber - generatedNumber) == 2 -> bet.betAmount.divide(BigDecimal.valueOf(2))
            else -> BigDecimal.ZERO
        }
    }

    @Test
    fun testPlaceBet_NotEnoughCredit() {
        val playerId = UUID.randomUUID()
        val betAmount = BigDecimal("1001")
        val betNumber = 3
        val player = Player("tfk", "tahsinfurkan", "keles")

        `when`(playerService.getPlayerById(playerId)).thenReturn(Mono.just(player))

        assertThrows(NotEnoughCreditException::class.java) {
            betService.placeBet(playerId, betAmount, betNumber).block()
        }
    }

    @Test
    fun testGetBets() {
        val player = Player("tfk", "tahsinfurkan", "keles")
        val playerId = player.id
        val betEntity1 = Bet(player, BigDecimal("100"), 3)
        val betEntity2 = Bet(player, BigDecimal("200"), 5)
        val bets = listOf(betEntity1, betEntity2)
        player.bets = bets
        BetTransactionRequestToResponseMapper.INSTANCE.toResponse(bets)
        val expectedResponse = BetTransactionRequestToResponseMapper.INSTANCE.toResponse(bets)

        `when`(playerService.getPlayerById(playerId)).thenReturn(Mono.just(player))
        `when`(betRepository.getBetsByPlayer(player)).thenReturn(bets)

        val mockPlayer = playerService.getPlayerById(playerId).block()

        val actualResponse = BetTransactionRequestToResponseMapper.INSTANCE.toResponse(mockPlayer?.bets)
        val result = betService.getBets(playerId)


        StepVerifier.create(result)
                .expectNext(expectedResponse, actualResponse)
    }

    @Test
    fun testCalculateWinnings() {
        val betEntity = Bet(Player("tfk", "tahsinfurkan", "keles"), BigDecimal("100"), 3)
        val generatedNumber = 3

        val result = betService.calculateWinnings(betEntity, generatedNumber)

        assertEquals(BigDecimal("1000"), result)
    }
}