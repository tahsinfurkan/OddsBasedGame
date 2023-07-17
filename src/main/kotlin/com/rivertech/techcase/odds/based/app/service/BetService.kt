package com.rivertech.techcase.odds.based.app.service

import com.rivertech.techcase.odds.based.app.common.TransactionType
import com.rivertech.techcase.odds.based.app.entity.Bet
import com.rivertech.techcase.odds.based.app.exceptions.NotEnoughCreditException
import com.rivertech.techcase.odds.based.app.mappers.BetEntityToPlaceBetResponseMapper
import com.rivertech.techcase.odds.based.app.mappers.BetRequestToBetEntityMapper
import com.rivertech.techcase.odds.based.app.mappers.BetTransactionRequestToResponseMapper
import com.rivertech.techcase.odds.based.app.model.BetResultsResponse
import com.rivertech.techcase.odds.based.app.model.PlaceBetResponse
import com.rivertech.techcase.odds.based.app.repository.BetRepository
import com.rivertech.techcase.odds.based.app.repository.PlayerRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.math.BigDecimal
import java.util.*

@Service
class BetService(
        private val betRepository: BetRepository,
        private val playerRepository: PlayerRepository,
        private val playerService: PlayerService,
        private val walletTransactionService: WalletTransactionService
) {

    fun placeBet(playerId: UUID, betAmount: BigDecimal, betNumber: Int): Mono<PlaceBetResponse> {
        return playerService.getPlayerById(playerId)
                .publishOn(Schedulers.boundedElastic())
                .flatMap { player ->
                    if (player.walletBalance.subtract(betAmount) < BigDecimal.ZERO) {
                        throw NotEnoughCreditException()
                    }
                    val betEntity = BetRequestToBetEntityMapper.INSTANCE.toEntity(player = player, betAmount = betAmount, betNumber = betNumber)
                    betRepository.save(betEntity)
                    player.walletBalance = player.walletBalance.subtract(betAmount)
                    playerRepository.save(player)
                    var transactionType = TransactionType.DEBIT
                    walletTransactionService.updateWallet(player, betAmount, betEntity, transactionType)
                    val generatedNumber = generateRandomNumber()
                    val betResult = calculateWinnings(betEntity, generatedNumber)
                    if (betResult.compareTo(BigDecimal.ZERO) != 0) {
                        player.walletBalance = player.walletBalance.add(betResult)
                        playerRepository.save(player)
                        transactionType = TransactionType.CREDIT
                        walletTransactionService.updateWallet(player, betResult, betEntity, transactionType)
                    }
                    val winning = betResult.subtract(betAmount)
                    if (winning.signum() == -1)
                        transactionType = TransactionType.DEBIT
                    Mono.just(BetEntityToPlaceBetResponseMapper.INSTANCE.toResponse(betEntity, generatedNumber, transactionType, winning, player.walletBalance))
                }
    }

    fun getBets(playerId: UUID): Mono<List<BetResultsResponse>> {
        return playerService.getPlayerById(playerId)
                .map { player ->
                    BetTransactionRequestToResponseMapper.INSTANCE.toResponse(player.bets)
                }
    }

    fun calculateWinnings(bet: Bet, generatedNumber: Int): BigDecimal {
        val betNumber = bet.betNumber
        return when {
            betNumber == generatedNumber -> bet.betAmount.multiply(BigDecimal.TEN)
            Math.abs(betNumber - generatedNumber) == 1 -> bet.betAmount.multiply(BigDecimal.valueOf(5))
            Math.abs(betNumber - generatedNumber) == 2 -> bet.betAmount.divide(BigDecimal.valueOf(2))
            else -> BigDecimal.ZERO
        }
    }

    private fun generateRandomNumber(): Int {
        return (1..10).random()
    }
}