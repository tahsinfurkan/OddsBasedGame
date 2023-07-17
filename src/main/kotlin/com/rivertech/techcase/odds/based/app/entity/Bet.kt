package com.rivertech.techcase.odds.based.app.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.rivertech.techcase.odds.based.app.annotation.Default
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Entity
data class Bet(

        @Id
        @Column(columnDefinition = "uuid")
        val id: UUID,
        @ManyToOne
        @JoinColumn(name = "player_id")
        val player: Player,
        val betAmount: BigDecimal,
        val betNumber: Int,
        @JsonBackReference
        @CreatedDate
        val createdDate: Date,
        @JsonBackReference
        @OneToMany(mappedBy = "bet", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        var transactions: List<WalletTransaction>? = null
) {
    @Default
    constructor(
            player: Player,
            betAmount: BigDecimal,
            betNumber: Int,
    ) : this(UUID.randomUUID(), player, betAmount, betNumber, Date.from(Instant.now()), mutableListOf())

    override fun toString(): String {
        return ""
    }

    override fun hashCode(): Int {
        return 0
    }
}