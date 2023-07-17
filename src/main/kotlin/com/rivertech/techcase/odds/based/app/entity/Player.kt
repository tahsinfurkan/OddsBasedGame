package com.rivertech.techcase.odds.based.app.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.rivertech.techcase.odds.based.app.annotation.Default
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Entity
data class Player(

        @Id
        @Column(columnDefinition = "uuid")
        val id: UUID,
        @Column(unique = true)
        val username: String,
        val name: String,
        val surname: String,
        var walletBalance: BigDecimal,
        @CreatedDate
        @JsonBackReference
        val createdDate: Date,
        @JsonBackReference
        @OneToMany(mappedBy = "player", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        var bets: List<Bet>? = null,
        @JsonBackReference
        @OneToMany(mappedBy = "player", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        var transactions: List<WalletTransaction>? = null
) {
    @Default
    constructor(
            username: String,
            name: String,
            surname: String
    ) : this(UUID.randomUUID(), username, name, surname, BigDecimal.valueOf(1000), Date.from(Instant.now()), mutableListOf(), mutableListOf())

    override fun toString(): String {
        return ""
    }

    override fun hashCode(): Int {
        return 0
    }
}