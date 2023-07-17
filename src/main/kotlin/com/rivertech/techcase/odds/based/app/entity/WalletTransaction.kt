package com.rivertech.techcase.odds.based.app.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.rivertech.techcase.odds.based.app.annotation.Default
import com.rivertech.techcase.odds.based.app.common.TransactionType
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Entity
data class WalletTransaction(

        @Id
        @Column(columnDefinition = "uuid")
        val id: UUID,
        @ManyToOne
        @JoinColumn(name = "player_id")
        val player: Player,
        @Enumerated(EnumType.STRING)
        val transactionType: TransactionType,
        val amount: BigDecimal,
        @CreatedDate
        @JsonBackReference
        val createdDate: Date,
        @ManyToOne
        @JoinColumn(name = "bet_id")
        val bet: Bet
) {
    @Default
    constructor(
            player: Player,
            transactionType: TransactionType,
            amount: BigDecimal,
            bet: Bet
    ) : this(UUID.randomUUID(), player, transactionType, amount, Date.from(Instant.now()), bet)

    override fun toString(): String {
        return ""
    }

    override fun hashCode(): Int {
        return 0
    }
}