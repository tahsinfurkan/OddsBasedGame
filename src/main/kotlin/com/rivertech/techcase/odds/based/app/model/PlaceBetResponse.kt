package com.rivertech.techcase.odds.based.app.model

import com.rivertech.techcase.odds.based.app.common.TransactionType
import java.math.BigDecimal

class PlaceBetResponse(
        var betAmount: BigDecimal,
        var betNumber: Int,
        var generatedNumber: Int,
        var transactionType: TransactionType,
        var winningAmount: BigDecimal,
        var walletBalance: BigDecimal
)