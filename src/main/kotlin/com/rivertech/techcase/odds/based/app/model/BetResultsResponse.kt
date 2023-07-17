package com.rivertech.techcase.odds.based.app.model

import com.rivertech.techcase.odds.based.app.dto.Transactions
import java.util.*

class BetResultsResponse(
        val id: UUID,
        val betNumber: Int,
        var transactions: List<Transactions>
)