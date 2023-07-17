package com.rivertech.techcase.odds.based.app.model

import java.math.BigDecimal
import java.util.*

class WinnersResponse(
        val id: UUID,
        val username: String,
        val winnings: BigDecimal
)