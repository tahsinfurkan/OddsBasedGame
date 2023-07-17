package com.rivertech.techcase.odds.based.app.model

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Positive
import lombok.Data
import java.math.BigDecimal

@Data
data class PlaceBetRequest(
        @field:Positive(message = "Bet amount must be greater than zero.")
        val betAmount: BigDecimal,
        @Min(value = 1)
        @Max(value = 10)
        val betNumber: Int
)