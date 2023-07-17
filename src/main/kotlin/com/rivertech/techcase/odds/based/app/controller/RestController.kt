package com.rivertech.techcase.odds.based.app.controller

import com.rivertech.techcase.odds.based.app.entity.Player
import com.rivertech.techcase.odds.based.app.model.*
import com.rivertech.techcase.odds.based.app.service.BetService
import com.rivertech.techcase.odds.based.app.service.PlayerService
import com.rivertech.techcase.odds.based.app.service.WalletTransactionService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
class RestController(private val playerService: PlayerService,
                     private val betService: BetService,
                     private val walletTransactionService: WalletTransactionService) {

    @PostMapping("/player")
    fun registerPlayer(@Valid @RequestBody registerPlayerRequest: RegisterPlayerRequest): Mono<RegisterPlayerResponse> {
        return playerService.registerPlayer(registerPlayerRequest)
    }

    @GetMapping("player/{playerId}")
    fun getPlayerById(@PathVariable playerId: UUID): Mono<Player> {
        return playerService.getPlayerById(playerId)
    }

    @PostMapping("/bet/{playerId}")
    fun placeBet(
            @PathVariable playerId: UUID,
            @Valid @RequestBody placeBetRequest: PlaceBetRequest
    ): Mono<PlaceBetResponse> {
        return betService.placeBet(playerId, placeBetRequest.betAmount, placeBetRequest.betNumber)
    }

    @GetMapping("/bet/{playerId}")
    fun getBets(
            @PathVariable playerId: UUID,
    ): Mono<List<BetResultsResponse>> {
        return betService.getBets(playerId)
    }

    @GetMapping("/wallet/{playerId}")
    fun placeBet(
            @PathVariable playerId: UUID,
    ): Flux<WalletTransactionsResponse> {
        return walletTransactionService.getWalletTransactions(playerId)
    }

    @GetMapping("winners")
    fun topWinners(
    ): Flux<WinnersResponse> {
        return walletTransactionService.topWinners()
    }
}