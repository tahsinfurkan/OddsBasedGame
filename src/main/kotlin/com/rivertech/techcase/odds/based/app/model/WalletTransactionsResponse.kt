package com.rivertech.techcase.odds.based.app.model

import com.rivertech.techcase.odds.based.app.common.TransactionType
import java.math.BigDecimal
import java.util.*

class WalletTransactionsResponse(
        var id: UUID,
        var transactionType: TransactionType,
        var amount: BigDecimal,
        var transactionTime: Date
)