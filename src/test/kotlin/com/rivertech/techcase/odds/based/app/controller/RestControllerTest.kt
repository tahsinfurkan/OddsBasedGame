package com.rivertech.techcase.odds.based.app.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.rivertech.techcase.odds.based.app.common.TransactionType
import com.rivertech.techcase.odds.based.app.dto.Transactions
import com.rivertech.techcase.odds.based.app.entity.Player
import com.rivertech.techcase.odds.based.app.model.*
import com.rivertech.techcase.odds.based.app.service.BetService
import com.rivertech.techcase.odds.based.app.service.PlayerService
import com.rivertech.techcase.odds.based.app.service.WalletTransactionService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@SpringBootTest
@AutoConfigureWebTestClient
class RestControllerTest {

    @Autowired
    private lateinit var client: WebTestClient

    @MockBean
    private lateinit var playerService: PlayerService

    @MockBean
    private lateinit var betService: BetService

    @MockBean
    private lateinit var walletTransactionService: WalletTransactionService

    private val objectMapper = ObjectMapper()

    private lateinit var playerId: UUID
    private lateinit var player: Player

    @BeforeEach
    fun setup() {
        player = Player("tfk", "tahsinfurkan", "keles")
        playerId = player.id
        val transactions = listOf(
                Transactions(UUID.randomUUID(), TransactionType.DEBIT, BigDecimal("100")),
                Transactions(UUID.randomUUID(), TransactionType.CREDIT, BigDecimal("200"))
        )
        val betResults = listOf(
                BetResultsResponse(UUID.randomUUID(), 3, transactions),
                BetResultsResponse(UUID.randomUUID(), 7, transactions)
        )

        val walletTransactions = listOf(
                WalletTransactionsResponse(UUID.randomUUID(), TransactionType.CREDIT, BigDecimal("100"), Date.from(Instant.now())),
                WalletTransactionsResponse(UUID.randomUUID(), TransactionType.DEBIT, BigDecimal("200"), Date.from(Instant.now()))
        )

        `when`(playerService.getPlayerById(playerId)).thenReturn(Mono.just(player))
        `when`(betService.getBets(playerId)).thenReturn(Mono.just(betResults))
        `when`(walletTransactionService.getWalletTransactions(playerId)).thenReturn(Flux.fromIterable(walletTransactions))
    }

    @Test
    fun testRegisterPlayer() {
        val request = RegisterPlayerRequest("tfk", "tahsinfurkan", "keles")
        val expectedResponse = RegisterPlayerResponse(player.id, player.username, player.name, player.surname)

        `when`(playerService.registerPlayer(request)).thenReturn(Mono.just(expectedResponse))

        client.post().uri("/player")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .json("""{"id": "${expectedResponse.id}"}""")
                .json("""{"username": "${expectedResponse.username}"}""")
                .json("""{"name": "${expectedResponse.name}"}""")
                .json("""{"surname": "${expectedResponse.surname}"}""")
    }

    @Test
    fun testGetPlayerById() {

        `when`(playerService.getPlayerById(playerId)).thenReturn(Mono.just(player))

        client.get().uri("/player/$playerId")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .json("""{"id": "${player.id}", "username": "${player.username}", "name": "${player.name}", "surname": "${player.surname}"}""")
    }

    @Test
    fun testPlaceBet() {
        val betAmount = BigDecimal("100")
        val betNumber = 3
        val request = PlaceBetRequest(betAmount, betNumber)
        val expectedResponse = PlaceBetResponse(betAmount, betNumber, 3, TransactionType.CREDIT, BigDecimal("1000"), BigDecimal("1900"))

        `when`(betService.placeBet(playerId, request.betAmount, request.betNumber)).thenReturn(Mono.just(expectedResponse))

        client.post().uri("/bet/$playerId")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.betAmount").isEqualTo(expectedResponse.betAmount)
                .jsonPath("$.betAmount").isEqualTo(expectedResponse.betAmount)
                .jsonPath("$.betNumber").isEqualTo(expectedResponse.betNumber)
                .jsonPath("$.generatedNumber").isEqualTo(expectedResponse.generatedNumber)
                .jsonPath("$.winningAmount").isEqualTo(expectedResponse.winningAmount)
                .jsonPath("$.walletBalance").isEqualTo(expectedResponse.walletBalance)
    }

    @Test
    fun testGetBets() {
        val transactions = listOf(
                Transactions(UUID.randomUUID(), TransactionType.DEBIT, BigDecimal("200")),
                Transactions(UUID.randomUUID(), TransactionType.CREDIT, BigDecimal("100"))
        )
        val betResults = listOf(
                BetResultsResponse(UUID.randomUUID(), 3, transactions)
        )

        `when`(betService.getBets(playerId)).thenReturn(Mono.just(betResults))

        client.get().uri("/bet/$playerId")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .json("""[${objectMapper.writeValueAsString(betResults[0])}]""")
    }

    @Test
    fun testGetWalletTransactions() {
        val walletTransactions = listOf(
                WalletTransactionsResponse(UUID.randomUUID(), TransactionType.CREDIT, BigDecimal("100"), Date.from(Instant.now()))
        )

        `when`(walletTransactionService.getWalletTransactions(playerId)).thenReturn(Flux.fromIterable(walletTransactions))

        client.get().uri("/wallet/$playerId")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(walletTransactions[0].id.toString())
                .jsonPath("$[0].transactionType").isEqualTo(walletTransactions[0].transactionType.toString())
                .jsonPath("$[0].amount").isEqualTo(walletTransactions[0].amount.toString())
    }

    @Test
    fun testTopWinners() {
        val winners = listOf(
                WinnersResponse(UUID.randomUUID(), "tfk", BigDecimal("1000")),
                WinnersResponse(UUID.randomUUID(), "fzk", BigDecimal("500"))
        )

        `when`(walletTransactionService.topWinners()).thenReturn(Flux.fromIterable(winners))

        client.get().uri("/winners")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(winners[0].id.toString())
                .jsonPath("$[0].username").isEqualTo(winners[0].username)
                .jsonPath("$[0].winnings").isEqualTo(winners[0].winnings.toString())
                .jsonPath("$[1].id").isEqualTo(winners[1].id.toString())
                .jsonPath("$[1].username").isEqualTo(winners[1].username)
                .jsonPath("$[1].winnings").isEqualTo(winners[1].winnings.toString())
    }

}