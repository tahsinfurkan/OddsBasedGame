package com.rivertech.techcase.odds.based.app.dto

import com.rivertech.techcase.odds.based.app.common.TransactionType
import java.math.BigDecimal
import java.util.*

class Transactions(
        val id: UUID,
        val transactionType: TransactionType,
        val amount: BigDecimal
)