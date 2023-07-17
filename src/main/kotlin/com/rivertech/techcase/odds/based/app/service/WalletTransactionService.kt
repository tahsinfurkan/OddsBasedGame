package com.rivertech.techcase.odds.based.app.service

import com.rivertech.techcase.odds.based.app.common.TransactionType
import com.rivertech.techcase.odds.based.app.entity.Bet
import com.rivertech.techcase.odds.based.app.entity.Player
import com.rivertech.techcase.odds.based.app.mappers.WalletEntityToWalletResponseMapper
import com.rivertech.techcase.odds.based.app.mappers.WalletRequestToWalletEntityMapper
import com.rivertech.techcase.odds.based.app.model.WalletTransactionsResponse
import com.rivertech.techcase.odds.based.app.model.WinnersResponse
import com.rivertech.techcase.odds.based.app.repository.WalletTransactionRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.math.BigDecimal
import java.util.*

@Service
class WalletTransactionService(private val walletTransactionRepository: WalletTransactionRepository,
                               private val playerService: PlayerService) {

    fun getWalletTransactions(playerId: UUID): Flux<WalletTransactionsResponse> {
        return playerService.getPlayerById(playerId)
                .publishOn(Schedulers.boundedElastic())
                .flatMapMany { player ->
                    Flux.fromIterable(walletTransactionRepository.getWalletTransactionsByPlayer(player))
                            .map { walletTransaction ->
                                WalletEntityToWalletResponseMapper.INSTANCE.toResponse(walletTransaction)
                            }
                }
    }

    fun updateWallet(player: Player, amount: BigDecimal, bet: Bet, transactionType: TransactionType) {
        walletTransactionRepository.save(
                WalletRequestToWalletEntityMapper.INSTANCE.toEntity(
                        player = player, transactionType = transactionType, amount = amount, bet = bet))
    }

    fun topWinners(): Flux<WinnersResponse> {
        return Flux.fromIterable(convertToWinnersList(walletTransactionRepository.getPlayerWinnings()))
    }

    private fun convertToWinnersList(winnerList: List<Array<Any>>): List<WinnersResponse> {
        return winnerList.map { winner ->
            val id = winner[0] as UUID
            val username = winner[1] as String
            val winnings = winner[2] as BigDecimal
            WinnersResponse(id, username, winnings)
        }
    }
}