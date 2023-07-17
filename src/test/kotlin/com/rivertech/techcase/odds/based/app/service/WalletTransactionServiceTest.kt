package com.rivertech.techcase.odds.based.app.service
import com.rivertech.techcase.odds.based.app.common.TransactionType
import com.rivertech.techcase.odds.based.app.entity.Bet
import com.rivertech.techcase.odds.based.app.entity.Player
import com.rivertech.techcase.odds.based.app.entity.WalletTransaction
import com.rivertech.techcase.odds.based.app.mappers.WalletEntityToWalletResponseMapper
import com.rivertech.techcase.odds.based.app.model.WinnersResponse
import com.rivertech.techcase.odds.based.app.repository.WalletTransactionRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.util.*

class WalletTransactionServiceTest{

    private val playerService: PlayerService = Mockito.mock(PlayerService::class.java)
    private val walletTransactionRepository: WalletTransactionRepository = Mockito.mock(WalletTransactionRepository::class.java)

    private val walletTransactionService = WalletTransactionService(walletTransactionRepository, playerService)

    @Test
    fun testGetWalletTransactions() {
        val playerId = UUID.randomUUID()
        val player = Player("tfk", "tahsinfurkan", "keles")
        val betEntity1 = Bet(player, BigDecimal("100"), 3)
        val betEntity2 = Bet(player, BigDecimal("200"), 7)
        val walletTransaction1 = WalletTransaction(player, TransactionType.DEBIT, BigDecimal("100"), betEntity1)
        val walletTransaction2 = WalletTransaction(player,  TransactionType.CREDIT, BigDecimal("200"), betEntity2)
        val walletTransactions = listOf(walletTransaction1, walletTransaction2)
        val expectedResponses = walletTransactions.map { WalletEntityToWalletResponseMapper.INSTANCE.toResponse(it) }

        `when`(playerService.getPlayerById(playerId)).thenReturn(Mono.just(player))
        `when`(walletTransactionRepository.getWalletTransactionsByPlayer(player)).thenReturn(walletTransactions)

        val result = walletTransactionService.getWalletTransactions(playerId).collectList()

        StepVerifier.create(result)
                .expectNext(expectedResponses)
    }

    @Test
    fun testTopWinners() {
        val winner1 = arrayOf(UUID.randomUUID(), "tfk", BigDecimal("1000"))
        val winner2 = arrayOf(UUID.randomUUID(), "fzk", BigDecimal("2000"))
        val winners = listOf(winner1, winner2)
        val expectedResponses = winners.map {
            WinnersResponse(it[0] as UUID, it[1] as String, it[2] as BigDecimal)
        }

        `when`(walletTransactionRepository.getPlayerWinnings()).thenReturn(winners as List<Array<Any>>)

        val result = walletTransactionService.topWinners().collectList()

        StepVerifier.create(result)
                .expectNext(expectedResponses)
    }
}